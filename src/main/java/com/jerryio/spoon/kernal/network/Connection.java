package com.jerryio.spoon.kernal.network;

import java.io.IOException;

import com.jerryio.spoon.kernal.network.protocol.Packet;
import com.jerryio.spoon.kernal.network.security.EncryptionManager;

import org.java_websocket.WebSocket;

public interface Connection {

    public WebSocket getWebSocket();

    public int getReceivedPackets();

    public void setReceivedPackets(int num);

    public int getSentPackets();

    public void setSentPackets(int num);

    public boolean isConnected();

    public EncryptionManager getEncryption();

    default public void sendPacket(Packet packet) throws IOException {
        try {
            getWebSocket().send(PacketEncoder.encode(packet, this));
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

}