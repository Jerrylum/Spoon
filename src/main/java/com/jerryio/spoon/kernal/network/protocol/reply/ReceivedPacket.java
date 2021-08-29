package com.jerryio.spoon.kernal.network.protocol.reply;

import java.nio.ByteBuffer;

public class ReceivedPacket implements ReplyPacket {

    private int receivedPacketId;

    public ReceivedPacket() {
    }

    public ReceivedPacket(int receivedPacketId) {
        this.receivedPacketId = receivedPacketId;
    }

    public int getReceivedPacketId() {
        return this.receivedPacketId;
    }

    public void read(ByteBuffer buf) {
        receivedPacketId = buf.getInt();
    }

    public void write(ByteBuffer buf) {
        buf.putInt(receivedPacketId);
    }
    
}
