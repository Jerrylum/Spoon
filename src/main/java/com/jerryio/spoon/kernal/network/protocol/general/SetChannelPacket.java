package com.jerryio.spoon.kernal.network.protocol.general;

import java.nio.ByteBuffer;

import com.jerryio.spoon.kernal.network.ByteBufferAddon;

public class SetChannelPacket implements GeneralPacket {

    private String channel;

    public SetChannelPacket() {
    }

    public SetChannelPacket(String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return this.channel;
    }

    public void read(ByteBuffer buf) {
        channel = ByteBufferAddon.readUtf(buf);
    }

    public void write(ByteBuffer buf) {
        ByteBufferAddon.writeUtf(buf, channel);
    }
    
}
