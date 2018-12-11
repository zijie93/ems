import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject, Observable, Subscription } from 'rxjs';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { WebSocketSubject, webSocket } from 'rxjs/webSocket';
import { map, retryWhen, takeUntil, filter, first, delay } from 'rxjs/operators';

import { environment as env } from '../../../environments';
import { Service } from './service';
import { Edge } from '../edge/edge';
import { Role } from '../type/role';
import { DefaultTypes } from './defaulttypes';
import { DefaultMessages } from './defaultmessages';
import { JsonrpcRequest, JsonrpcResponse, JsonrpcMessage, JsonrpcNotification, JsonrpcResponseError, JsonrpcResponseSuccess } from './jsonrpc/base';
import { WsData } from './wsdata';
import { AuthenticateWithSessionIdNotification } from './jsonrpc/notification/authenticatedWithSessionIdNotification';
import { CurrentDataNotification } from './jsonrpc/notification/currentDataNotification';
import { EdgeRpcNotification } from './jsonrpc/notification/edgeRpcNotification';
import { CurrentDataAndSummary_2018_8 } from '../edge/currentdata.2018.8';

@Injectable()
export class Websocket {
  public static readonly TIMEOUT = 15000;
  private static readonly DEFAULT_EDGEID = 0;
  private readonly wsdata = new WsData();

  // holds references of edges (=key) to Edge objects (=value)
  private _edges: BehaviorSubject<{ [edgeId: string]: Edge }> = new BehaviorSubject({});
  public get edges() {
    return this._edges;
  }

  // holds the currently selected edge
  private _currentEdge: BehaviorSubject<Edge> = new BehaviorSubject<Edge>(null);
  public get currentEdge() {
    return this._currentEdge;
  }

  private socket: WebSocketSubject<any>;
  public status: DefaultTypes.ConnectionStatus = "connecting";
  public isWebsocketConnected: BehaviorSubject<boolean> = new BehaviorSubject(false);

  private username: string = "";
  // private messages: Observable<string>;
  private queryreply = new Subject<{ id: string[] }>();
  private stopOnInitialize: Subject<void> = new Subject<void>();

  // tracks which message id (=key) is connected with which edgeId (=value)
  private pendingQueryReplies: { [id: string]: string } = {};

  constructor(
    private router: Router,
    private service: Service,
  ) {
    // try to auto connect using token or session_id
    setTimeout(() => {
      this.connect();
    })
  }

  /**
   * Parses the route params and sets the current edge
   */
  public setCurrentEdge(route: ActivatedRoute): Subject<Edge> {
    let onTimeout = () => {
      // Timeout: redirect to index
      if (this.router.url != '/settings' && this.router.url != '/about') {
        this.router.navigate(['/index']);
        subscription.unsubscribe();
      }
    }

    let edgeId = route.snapshot.params["edgeId"];
    let subscription = this.edges
      .pipe(filter(edges => edgeId in edges),
        first(),
        map(edges => edges[edgeId]))
      .subscribe(edge => {
        if (edge == null || !edge.isOnline) {
          onTimeout();
        } else {
          // set current edge
          this.currentEdge.next(edge);
          edge.markAsCurrentEdge();
        }
      }, error => {
        console.error("Error while setting current edge: ", error);
      })
    setTimeout(() => {
      let edge = this.currentEdge.getValue();
      if (edge == null || !edge.isOnline) {
        onTimeout();
      }
    }, Websocket.TIMEOUT);
    return this.currentEdge;
  }

  /**
   * Clears the current edge
   */
  public clearCurrentEdge() {
    this.currentEdge.next(null);
  }

  /**
   * Opens a connection using a stored token or a cookie with a session_id for this websocket. Called once by constructor
   */
  private connect(): BehaviorSubject<boolean> {
    if (this.socket != null) {
      return this.isWebsocketConnected;
    }

    if (env.debugMode) {
      console.info("Websocket connect to URL [" + env.url + "]");
    }

    this.socket = webSocket({
      url: env.url,
      openObserver: {
        next: (value) => {
          if (env.debugMode) {
            console.info("Websocket connection opened");
          }
          this.isWebsocketConnected.next(true);
          if (this.status == 'online') {
            this.service.notify({
              message: "Connection lost. Trying to reconnect.", // TODO translate
              type: 'warning'
            });
            // TODO show spinners everywhere
            this.status = 'connecting';
          } else {
            this.status = 'waiting for authentication';
          }
        }
      },
      closeObserver: {
        next: (value) => {
          if (env.debugMode) {
            console.info("Websocket connection closed");
          }
          this.isWebsocketConnected.next(false);
        }
      }
    });

    // this.socket = WebSocketSubject.create({
    //   url: env.url,
    //   openObserver: {
    //     next: (value) => {
    //       if (env.debugMode) {
    //         console.info("Websocket connection opened");
    //       }
    //       this.isWebsocketConnected.next(true);
    //       if (this.status == 'online') {
    //         this.service.notify({
    //           message: "Connection lost. Trying to reconnect.", // TODO translate
    //           type: 'warning'
    //         });
    //         // TODO show spinners everywhere
    //         this.status = 'connecting';
    //       } else {
    //         this.status = 'waiting for authentication';
    //       }
    //     }
    //   },
    //   closeObserver: {
    //     next: (value) => {
    //       if (env.debugMode) {
    //         console.info("Websocket connection closed");
    //       }
    //       this.isWebsocketConnected.next(false);
    //     }
    //   }
    // });

    this.socket.pipe(
      retryWhen(errors => {
        console.warn("Websocket was interrupted. Retrying in 2 seconds.");
        return errors.pipe(delay(2000));

      })).subscribe(originalMessage => {
        // called on every receive of message from server
        if (env.debugMode) {
          console.info("RECV", originalMessage);
        }

        let message: JsonrpcRequest | JsonrpcNotification | JsonrpcResponseSuccess | JsonrpcResponseError;
        try {
          message = JsonrpcMessage.from(originalMessage);
        } catch (e) {
          // handle deprecated non-JSON-RPC messages
          message = this.handleNonJsonrpcMessage(originalMessage, e);
        }

        if (message instanceof JsonrpcRequest) {
          console.info("Request", message);

          // handle JSON-RPC Request
          this.onRequest(message);

        } else if (message instanceof JsonrpcResponse) {
          // handle JSON-RPC Response
          this.onResponse(message);

        } else if (message instanceof JsonrpcNotification) {
          // handle JSON-RPC Notification
          this.onNotification(message);
        }


        // /*
        //  * Authenticate
        //  */
        // if ("authenticate" in message && "mode" in message.authenticate) {
        //   let mode = message.authenticate.mode;
        //   } else {
        //     // authentication denied -> close websocket
        //     this.status = "failed";
        //     this.service.removeToken();
        //     this.initialize();
        //     if (env.backend === "OpenEMS Backend") {
        //       if (env.production) {
        //         window.location.href = "/web/login?redirect=/m/index";
        //       } else {
        //         console.info("would redirect...");
        //       }
        //     } else if (env.backend === "OpenEMS Edge") {
        //       this.router.navigate(['/index']);
        //     }
        //   }
        // }

        // /*
        //  * Query reply
        //  */
        // if ("messageId" in message && "ui" in message.messageId) {
        //   // Receive a reply with a message id -> find edge and forward to edges' replyStream
        //   let messageId = message.messageId.ui;
        //   for (let edgeId in this.replyStreams) {
        //     if (messageId in this.replyStreams[edgeId]) {
        //       this.replyStreams[edgeId][messageId].next(message);
        //       break;
        //     }
        //   }
        // }

        // /*
        //  * receive notification
        //  */
        // if ("notification" in message) {
        //   let notification = message.notification;
        //   let n: DefaultTypes.Notification;
        //   let notify: boolean = true;
        //   if ("code" in notification) {
        //     // handle specific notification codes - see Java source for details
        //     let code = notification.code;
        //     let params = notification.params;
        //     if (code == 100 /* edge disconnected -> mark as offline */) {
        //       let edgeId = params[0];
        //       if (edgeId in this.edges.getValue()) {
        //         this.edges.getValue()[edgeId].setOnline(false);
        //       }
        //     } else if (code == 101 /* edge reconnected -> mark as online */) {
        //       let edgeId = params[0];
        //       if (edgeId in this.edges.getValue()) {
        //         let edge = this.edges.getValue()[edgeId];
        //         edge.setOnline(true);
        //       }
        //     } else if (code == 103 /* authentication by token failed */) {
        //       let token: string = params[0];
        //       if (token !== "") {
        //         // remove old token
        //         this.service.removeToken();
        //       }
        //       // ask for authentication info
        //       this.status = "waiting for authentication";
        //       notify = false;
        //       setTimeout(() => {
        //         this.clearCurrentEdge();
        //         this.router.navigate(["/index"]);
        //       });
        //     }
        //   }
        //   if (notify) {
        //     this.service.notify(<DefaultTypes.Notification>notification);
        //   }
        // }
      }, error => {
        this.onError(error);

      }, () => {
        this.onClose();

      })
    return this.isWebsocketConnected;
  }

  /**
   * Reset everything to default
   */
  private initialize() {
    this.stopOnInitialize.next();
    this.stopOnInitialize.complete();
    this.edges.next({});
  }

  /**
   * Opens the websocket and logs in
   */
  public logIn(password: string) {
    if (this.isWebsocketConnected.getValue()) {
      // websocket was connected
      this.send(DefaultMessages.authenticateLogin(password));
    } else {
      // websocket was NOT connected
      this.connect()
        .pipe(takeUntil(this.stopOnInitialize),
          filter(isConnected => isConnected),
          first())
        .subscribe(isConnected => {
          setTimeout(() => {
            this.send(DefaultMessages.authenticateLogin(password))
          }, 500);
        });
    }
  }

  /**
   * Logs out and closes the websocket
   */
  public logOut() {
    // TODO this is kind of working for now... better would be to not close the websocket but to handle session validity serverside
    this.send(DefaultMessages.authenticateLogout());
    this.status = "waiting for authentication";
    this.service.removeToken();
    this.initialize();
  }

  /**
   * Sends a message to the websocket
   */
  // TODO deprecated
  public send(message: any): void {
    if (env.debugMode) {
      console.info("SEND: ", message);
    }
    this.socket.next(message);
  }

  /**
   * Sends a JSON-RPC request to a Websocket and registers a callback.
   * 
   * @param request 
   * @param responseCallback 
   */
  public sendRequest(request: JsonrpcRequest, responseCallback: (response: JsonrpcResponse) => void) {
    this.wsdata.sendRequest(this.socket, request, responseCallback);
  }

  /**
   * Sends a JSON-RPC notification to a Websocket.
   * 
   * @param notification 
   */
  public sendNotification(notification: JsonrpcNotification) {
    this.wsdata.sendNotification(this.socket, notification);
  }

  /**
   * Handle deprecated non-JSON-RPC message
   * 
   * @param originalMessage 
   * @param e 
   */
  private handleNonJsonrpcMessage(originalMessage: any, e: any): JsonrpcRequest | JsonrpcNotification | JsonrpcResponseSuccess | JsonrpcResponseError {
    throw new Error("Unhandled Non-JSON-RPC message: " + e);
  }

  /**
   * Handle new JSON-RPC Request
   * 
   * @param message 
   * @param responseCallback 
   */
  private onRequest(message: JsonrpcRequest): void {
    // responseCallback.apply(...)
    console.log("On Request: " + message);
  }

  /**
   * Handle new JSON-RPC Response
   * 
   * @param message 
   */
  private onResponse(response: JsonrpcResponse): void {
    this.wsdata.handleJsonrpcResponse(response);
  }

  /**
   * Handle new JSON-RPC Notification
   * 
   * @param message 
   */
  private onNotification(message: JsonrpcNotification): void {
    switch (message.method) {
      case AuthenticateWithSessionIdNotification.METHOD:
        this.handleAuthenticateWithSessionId(message as AuthenticateWithSessionIdNotification);
        break;

      case EdgeRpcNotification.METHOD:
        this.handleEdgeRpcNotification(message as EdgeRpcNotification);
        break;
    }
  }

  /**
   * Handle Websocket error.
   * 
   * @param error
   */
  private onError(error: any): void {
    console.error("Websocket error", error);
  }

  /**
   * Handle Websocket closed event.
   */
  private onClose(): void {
    console.info("Websocket closed.");
  }

  /**
   * Handles a AuthenticateWithSessionIdNotification.
   * 
   * @param message 
   */
  private handleAuthenticateWithSessionId(message: AuthenticateWithSessionIdNotification): void {
    let p = message.params;
    this.status = "online"

    // received login token -> save in cookie
    this.service.setToken(p.token);

    // Metadata
    let edges = p.edges;
    let newEdges = {};
    for (let edge of edges) {
      let newEdge = new Edge(
        edge.id,
        edge.comment,
        edge.producttype,
        ("version" in edge) ? edge["version"] : "0.0.0",
        Role.getRole(edge.role),
        edge.isOnline
      );
      newEdges[newEdge.id] = newEdge;
    }
    this.edges.next(newEdges);
  }

  /**
   * Handles an EdgeRpcNotification.
   * 
   * @param message 
   */
  private handleEdgeRpcNotification(edgeRpcNotification: EdgeRpcNotification): void {
    let edgeId = edgeRpcNotification.params.edgeId;
    let message = edgeRpcNotification.params.payload;

    switch (message.method) {
      case CurrentDataNotification.METHOD:
        this.handleCurrentDataNotification(edgeId, message as CurrentDataNotification);
        break;
    }
  }

  /**
   * Handles a CurrentDataNotification.
   * 
   * @param message 
   */
  private handleCurrentDataNotification(edgeId: string, message: CurrentDataNotification): void {
    let edges = this.edges.getValue();

    if (edgeId in edges) {
      let edge = edges[edgeId];
      let values = {};
      for (let channel in message.params) {
        let channelAddress = channel.split('/', 2);
        let componentId = channelAddress[0];
        let channelId = channelAddress[1];
        if (!(componentId in values)) {
          values[componentId] = {};
        }
        values[componentId][channelId] = message.params[channel];
      }

      let data = new CurrentDataAndSummary_2018_8(edge, values, edge.config.getValue());
      edge.currentData.next(data);
    }
  }

}
