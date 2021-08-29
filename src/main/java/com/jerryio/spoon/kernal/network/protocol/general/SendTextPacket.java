package com.jerryio.spoon.kernal.network.protocol.general;

import java.nio.ByteBuffer;

import com.jerryio.spoon.kernal.network.ByteBufferAddon;

public class SendTextPacket implements GeneralPacket {

    private String message;

    public SendTextPacket() {
    }

    public SendTextPacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void read(ByteBuffer buf) {
        message = ByteBufferAddon.readUtf(buf);
    }

    public void write(ByteBuffer buf) {
        ByteBufferAddon.writeUtf(buf, message);
    }
    
}