package com.jerryio.spoon.kernal.event.client;

import org.java_websocket.handshake.ServerHandshake;

public class ConnectionOpenEvent {
    private ServerHandshake handshakedata;

    public ConnectionOpenEvent(ServerHandshake handshakedata) {
        this.handshakedata = handshakedata;
    }

    public ServerHandshake getHandshakedata() {
        return this.handshakedata;
    }

}
