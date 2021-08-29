package com.jerryio.spoon.kernal.event.server;

import org.java_websocket.WebSocket;

public class ServerErrorEvent {
    private WebSocket socket;
    private Exception exception;

    public boolean isSocketProblem() {
        return this.getSocket() != null;
    }

    public WebSocket getSocket() {
        return socket;
    }

    public Exception getException() {
        return this.exception;
    }

    public ServerErrorEvent(WebSocket socket, Exception exception) {
        this.socket = socket;
        this.exception = exception;
    }

}
