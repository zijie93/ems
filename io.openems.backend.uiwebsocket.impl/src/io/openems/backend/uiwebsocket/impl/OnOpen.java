package io.openems.backend.uiwebsocket.impl;

import java.util.*;

import io.openems.common.accesscontrol.AuthenticationException;
import io.openems.common.accesscontrol.RoleId;
import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import io.openems.backend.metadata.api.Edge;
import io.openems.common.exceptions.OpenemsException;
import io.openems.common.jsonrpc.notification.AuthenticateWithSessionIdFailedNotification;
import io.openems.common.jsonrpc.notification.AuthenticateWithSessionIdNotification;
import io.openems.common.jsonrpc.shared.EdgeMetadata;

public class OnOpen implements io.openems.common.websocket.OnOpen {

    private final Logger log = LoggerFactory.getLogger(OnOpen.class);
    private final UiWebsocketImpl parent;

    public OnOpen(UiWebsocketImpl parent) {
        this.parent = parent;
    }

    @Override
    public void run(WebSocket ws, JsonObject handshake) throws OpenemsException {
        // get websocket attachment
        WsData wsData = ws.getAttachment();

        // login using session_id from the handshake
        Optional<String> sessionIdOpt = io.openems.common.websocket.OnOpen.getFieldFromHandshakeCookie(handshake,
                "token");

        // get token from cookie or generate new token
        sessionIdOpt.ifPresent(cookieToken -> {
            try {
                // read token from Cookie
                UUID token = UUID.fromString(cookieToken);

                // login using token from the cookie
                RoleId roleId = this.parent.accessControl.login(token);

                // token from cookie is valid -> authentication successful
                // store user in attachment
                wsData.setRoleId(roleId);
                // send authentication notification

                Set<String> edgeIds = this.parent.accessControl.getEdgeIds(roleId);
                List<EdgeMetadata> metadatas = new ArrayList<>();
                for (String edgeId : edgeIds) {
                    Optional<Edge> edgeOpt = this.parent.metadata.getEdge(edgeId);
                    if (edgeOpt.isPresent()) {
                        Edge e = edgeOpt.get();
                        metadatas.add(new EdgeMetadata(//
                                e.getId(),
                                e.getComment(),
                                e.getProducttype(),
                                e.getVersion(),
                                roleId,
                                e.isOnline()
                        ));
                    }
                }

                AuthenticateWithSessionIdNotification notification = new AuthenticateWithSessionIdNotification(
                        token, metadatas);
                this.parent.server.sendMessage(ws, notification);

                // log
                Optional<String> userName = this.parent.accessControl.getUsernameForToken(token);
                if (userName.isPresent()) {
                    this.parent.logInfo(this.log, "User [" + userName.get() + "] logged in by token");
                } else {
                    this.parent.logInfo(this.log, "Unknown user logged in by token (" + token + ").");
                }
            } catch (IllegalArgumentException e) {
                this.parent.logWarn(this.log, "Cookie Token [" + cookieToken + "] is not a UUID: " + e.getMessage());
            } catch (AuthenticationException e) {
                // automatic authentication was not possible -> notify client
                this.parent.server.sendMessage(ws, new AuthenticateWithSessionIdFailedNotification());
            }
        });
    }

}
