package com.jerryio.spoon.kernal.event.server;

import com.jerryio.spoon.kernal.server.RemoteDevice;

public class ClientDisconnectedEvent {
    private RemoteDevice device;
    private int code;
    private String reason;
    private boolean remote;

    public RemoteDevice getWebSocket() {
        return device;
    }

    public int getCode() {
        return this.code;
    }

    public String getReason() {
        return this.reason;
    }

    public boolean isRemote() {
        return this.remote;
    }

    public ClientDisconnectedEvent(RemoteDevice device, int code, String reason, boolean remote) {
        this.device = device;
        this.code = code;
        this.reason = reason;
        this.remote = remote;
    }
}
