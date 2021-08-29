package com.jerryio.spoon.kernal.event.server;

import com.jerryio.spoon.kernal.server.RemoteDevice;

import org.java_websocket.handshake.ClientHandshake;

public class ClientConnectedEvent {
    private RemoteDevice device;
    private ClientHandshake handshakedata;

    public ClientConnectedEvent(RemoteDevice device, ClientHandshake handshakedata) {
        this.device = device;
        this.handshakedata = handshakedata;
    }

    public RemoteDevice getDevice() {
        return device;
    }

    public ClientHandshake getHandshakedata() {
        return this.handshakedata;
    }
}
