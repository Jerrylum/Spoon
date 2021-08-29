package com.jerryio.spoon.kernal.client;

import java.io.IOException;
import java.net.URI;

import com.jerryio.spoon.kernal.network.protocol.general.SendTextPacket;
import com.jerryio.spoon.kernal.network.protocol.general.SetChannelPacket;

public class ClientDevice {

    private ClientConnection connection;
    private String channel;
    private int lastMessagePacketId;

    public ClientDevice(URI serverUri, ClientListener listener) {
        listener.setClient(this);
        this.connection = new ClientConnection(serverUri);
        this.connection.addEventListener(listener);
        this.connection.connect();
        this.channel = "";
    }

    public ClientConnection getConnection() {
        return connection;
    }

    public boolean isEncrypted() {
        return connection.getEncryption().getAESEncryptCipher() != null;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String newChannel) throws IOException {
        connection.sendPacket(new SetChannelPacket(newChannel));
        this.channel = newChannel;
    }

    public int getLastMessagePacketId() {
        return this.lastMessagePacketId;
    }

    public void sendTextMessage(String msg) throws IOException {
        lastMessagePacketId = connection.getSentPackets();
        connection.sendPacket(new SendTextPacket(msg));
    }
}
