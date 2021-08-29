package com.jerryio.spoon.kernal.event.client;

public class ClientErrorEvent {
    private Exception exception;

    public Exception getException() {
        return this.exception;
    }

    public ClientErrorEvent(Exception exception) {
        this.exception = exception;
    }

}
