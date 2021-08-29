package com.jerryio.spoon.kernal.event.client;

public class ConnectionCloseEvent {
    
    private int code;
    private String reason;
    private boolean remote;

    public int getCode() {
        return this.code;
    }

    public String getReason() {
        return this.reason;
    }

    public boolean isRemote() {
        return this.remote;
    }

    public ConnectionCloseEvent(int code, String reason, boolean remote) {
        this.code = code;
        this.reason = reason;
        this.remote = remote;
    }

}
