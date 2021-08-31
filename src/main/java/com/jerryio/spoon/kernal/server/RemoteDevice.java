package com.jerryio.spoon.kernal.server;

import java.io.IOException;

import com.jerryio.spoon.kernal.network.protocol.general.SendTextPacket;

import org.java_websocket.WebSocket;

public class RemoteDevice {
    private RemoteConnection connection;
    private String channel;
    private int id;

    public RemoteDevice(int id, WebSocket socket) {
        this.id = id;
        this.connection = new RemoteConnection(socket);
        this.channel = "";
    }

    public RemoteConnection getConnection() {
        return connection;
    }

    public boolean isEncrypted() {
        return connection.getEncryption().getAESEncryptCipher() != null;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String newChannel) {
        this.channel = newChannel;
    }

    public int getId() {
        return id;
    }

    public void sendTextMessage(String msg) throws IOException {
        connection.sendPacket(new SendTextPacket(msg));
    }
}
