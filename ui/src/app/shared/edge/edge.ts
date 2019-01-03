import { Subject, BehaviorSubject, ReplaySubject, Observer, Observable } from 'rxjs';
import { first, map, combineLatest } from 'rxjs/operators';
import { cmp } from 'semver-compare-multi';

import { ConfigImpl } from './config';
import { CurrentDataAndSummary } from './currentdata';
import { DefaultMessages } from '../service/defaultmessages';
import { DefaultTypes } from '../service/defaulttypes';
import { Role } from '../type/role';
import { SubscribeChannelsRequest } from '../service/jsonrpc/request/subscribeChannelsRequest';
import { JsonrpcRequest, JsonrpcResponse } from '../service/jsonrpc/base';
import { EdgeRpcRequest } from '../service/jsonrpc/request/edgeRpcRequest';
import { ChannelAddress } from '../type/channeladdress';
import { EdgeRpcResponse } from '../service/jsonrpc/response/edgeRpcResponse';
import { Websocket } from '../service/websocket';

export class Log {
  timestamp: number;
  time: string = "";
  level: string;
  color: string = "black";
  source: string;
  message: string;
}

export class Edge {

  constructor(
    public readonly id: string,
    public readonly comment: string,
    public readonly producttype: string,
    public readonly version: string,
    public readonly role: Role,
    public isOnline: boolean
  ) { }

  // holds currently subscribed channels, identified by source id
  private subscribedChannels: { [sourceId: string]: ChannelAddress[] } = {};

  // holds current data
  public currentData: Subject<CurrentDataAndSummary> = new Subject<CurrentDataAndSummary>();

  // holds log
  public log: Observable<DefaultTypes.Log>;

  // holds config
  public config: BehaviorSubject<ConfigImpl> = new BehaviorSubject<ConfigImpl>(null);

  public getConfig(websocket: Websocket): BehaviorSubject<ConfigImpl> {
    if (this.config.value == null) {
      // websocket.sendRequest() TODO
    }
    return this.config;
  }

  public event = new Subject<Notification>();
  public address: string;

  //public historykWh = new BehaviorSubject<any[]>(null);
  private state: 'active' | 'inactive' | 'test' | 'installed-on-stock' | '' = '';
  private subscribeCurrentDataChannels: string[] = [];

  /**
   * Called by Service, when this Edge is set as currentEdge.
   */
  public markAsCurrentEdge() {
    if (this.config.value == null) {
      this.refreshConfig();
    }
  }

  /**
   * Refresh the config.
   */
  public refreshConfig() {
    // websocket.sendRequest();

    // // TODO use sendMessageWithReply()
    // let message = DefaultMessages.configQuery(this.edgeId);
    // let messageId = message.messageId.ui;
    // this.replyStreams[messageId] = new Subject<DefaultMessages.Reply>();
    // this.send(message);
    // // wait for reply
    // this.replyStreams[messageId].pipe(first()).subscribe(reply => {
    //   let config = (<DefaultMessages.ConfigQueryReply>reply).config;
    //   let configImpl
    //   if (this.isVersionAtLeast('2018.8')) {
    //     configImpl = new ConfigImpl_2018_8(this, config);
    //   } else {
    //     configImpl = new ConfigImpl_2018_7(this, config);
    //   }
    //   this.config.next(configImpl);
    //   this.replyStreams[messageId].unsubscribe();
    //   delete this.replyStreams[messageId];
    // });
    // // TODO add timeout
    // return this.config;
  }

  /**
   * Sends a message to websocket
   */
  public send(value: any): void {
    console.warn("Edge.send()", value);
  }

  private sendMessageWithReply(message: DefaultTypes.IdentifiedMessage): void {
    console.warn("Edge.sendMessageWithReply()", message);
    // let messageId: string = message.messageId.ui;
    // this.replyStreams[messageId] = new Subject<DefaultMessages.Reply>();
    // this.send(message);
    // return this.replyStreams[messageId];
  }

  private removeReplyStream(reply: DefaultMessages.Reply) {
    console.warn("Edge.removeReplyStream()", reply);
    // let messageId: string = reply.messageId.ui;
    // this.replyStreams[messageId].unsubscribe();
    // delete this.replyStreams[messageId];
  }

  /**
   * Add Channels to subscription
   * 
   * @param websocket the Websocket
   * @param id        a unique ID for this subscription (e.g. the component selector)
   * @param channels  the subscribed Channel-Adresses
   */
  public subscribeChannels(websocket: Websocket, id: string, channels: ChannelAddress[]) {
    this.subscribedChannels[id] = channels;
    this.sendSubscribeChannels(websocket);
  }

  /**
   * Removes Channels from subscription
   * 
   * @param ws the Websocket
   * @param id the unique ID for this subscription
   */
  public unsubscribeChannels(ws: Websocket, id: string) {
    delete this.subscribedChannels[id];
    this.sendSubscribeChannels(ws);
  }

  /**
   * Sends a SubscribeChannelsRequest for all Channels in 'this.subscribedChannels'
   * 
   * @param ws the Websocket
   */
  private sendSubscribeChannels(ws: Websocket) {
    // merge channels from currentDataSubscribes
    let channels: ChannelAddress[] = [];
    for (let componentId in this.subscribedChannels) {
      channels.push.apply(channels, this.subscribedChannels[componentId]);
    }
    let request = new SubscribeChannelsRequest(channels);
    this.sendRequest(ws, request, (response) => {
      console.log("response to subscribe: ", response);
    });
  }

  /**
   * Sends a JSON-RPC Request, wrapped in a EdgeRpcRequest with the Edge-ID.
   * 
   * @param ws               the Websocket
   * @param request          the JSON-RPC Request
   * @param responseCallback the JSON-RPC Response callback
   */
  public sendRequest(ws: Websocket, request: JsonrpcRequest, responseCallback: (response: JsonrpcResponse) => void): void {
    let wrap = new EdgeRpcRequest(this.id, request);
    ws.sendRequest(wrap, (response) => {
      if ("result" in response && "payload" in response['result']) {
        // unwrap
        responseCallback(response['result']['payload']);
      } else {
        // TODO: not successful: wrap in JSON-RPC Error
        responseCallback(response);
      }
    });
  }

  /**
   * Query data
   */
  // TODO: kWh: this.getkWhResult(this.getImportantChannels())
  // public historicDataQuery(fromDate: Date, toDate: Date, channels: DefaultTypes.ChannelAddresses): void {
  // console.warn("Edge.historicDataQuery()", fromDate, toDate, channels);
  // let timezone = new Date().getTimezoneOffset() * 60;
  // let replyStream = this.sendMessageWithReply(DefaultMessages.historicDataQuery(this.edgeId, fromDate, toDate, timezone, channels));
  // // wait for reply
  // return new Promise((resolve, reject) => {
  //   replyStream.pipe(first()).subscribe(reply => {
  //     let historicData = (reply as DefaultMessages.HistoricDataReply).historicData;
  //     this.removeReplyStream(reply);
  //     resolve(historicData);
  //   });
  // })
  // }

  /**
   * Mark this edge as online or offline
   * 
   * @param isOnline 
   */
  public setOnline(isOnline: boolean) {
    this.isOnline = isOnline;
  }

  /**
   * Subscribe to log
   */
  public subscribeLog(): void {
    console.warn("Edge.subscribeLog()");
    // const message = DefaultMessages.logSubscribe(this.edgeId);
    // let replyStream = this.sendMessageWithReply(message);
    // return { messageId: message.messageId.ui, logs: replyStream.pipe(map(message => message.log as DefaultTypes.Log)) };
  }

  /**
   * Unsubscribe from log
   */
  public unsubscribeLog(messageId: string) {
    console.warn("Edge.unsubscribeLog()");
    // let message = DefaultMessages.logUnsubscribe(messageId, this.edgeId);
    // this.send(message);
  }

  /**
   * System Execute
   */
  public systemExecute(password: string, command: string, background: boolean, timeout: number): void {
    console.warn("Edge.systemExecute()", password, command);
    // let replyStream = this.sendMessageWithReply(DefaultMessages.systemExecute(this.edgeId, password, command, background, timeout));
    // // wait for reply
    // return new Promise((resolve, reject) => {
    //   replyStream.pipe(first()).subscribe(reply => {
    //     let output = (reply as DefaultMessages.SystemExecuteReply).system.output;
    //     this.removeReplyStream(reply);
    //     resolve(output);
    //   });
    // })
  }

  /**
   * Returns whether the given version is higher than the Edge' version
   * 
   * Example: {{ edge.isVersionAtLeast('2018.9') }}
   * 
   * @param version 
   */
  public isVersionAtLeast(version: string): boolean {
    return cmp(this.version, version) >= 0;
  }
}