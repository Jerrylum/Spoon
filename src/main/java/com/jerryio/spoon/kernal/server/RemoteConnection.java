package com.jerryio.spoon.kernal.server;

import com.jerryio.spoon.kernal.network.Connection;
import com.jerryio.spoon.kernal.network.security.EncryptionManager;

import org.java_websocket.WebSocket;

public class RemoteConnection implements Connection {
    private WebSocket socket;
    private EncryptionManager encryption;

    private int receivedPackets = 0;
    private int sentPackets = 1;

    public RemoteConnection(WebSocket socket) {
        this.socket = socket;
        this.encryption = new EncryptionManager();
    }

    @Override
    public WebSocket getWebSocket() {
        return socket;
    }
    
    @Override
    public int getReceivedPackets() {
        return receivedPackets;
    }

    @Override
    public void setReceivedPackets(int num) {
        this.receivedPackets = num;
    }

    @Override
    public int getSentPackets() {
        return sentPackets;
    }

    @Override
    public void setSentPackets(int num) {
        this.sentPackets = num;
    }

    @Override
    public boolean isConnected() {
        return socket.isOpen();
    }

    @Override
    public EncryptionManager getEncryption() {
        return encryption;
    }
    
}
