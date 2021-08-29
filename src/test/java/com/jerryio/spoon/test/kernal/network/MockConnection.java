package com.jerryio.spoon.test.kernal.network;

import com.jerryio.spoon.kernal.network.Connection;
import com.jerryio.spoon.kernal.network.security.EncryptionManager;

import org.java_websocket.WebSocket;

public class MockConnection implements Connection {

    public EncryptionManager encryption = new EncryptionManager();

    public int receivedPackets = 2;
    public int sentPackets = 3;

    public WebSocket socket;

    public boolean isConnected = true;

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
        return isConnected;
    }

    @Override
    public EncryptionManager getEncryption() {
        return encryption;
    }
}
