package com.jerryio.spoon.kernal.network.protocol.handshake;

import java.nio.ByteBuffer;
import java.security.PublicKey;

import com.jerryio.spoon.kernal.network.ByteBufferAddon;

public class RequireEncryptionPacket implements HandshakePacket {

    private PublicKey serverPublicKey;

    public RequireEncryptionPacket() {

    }

    public RequireEncryptionPacket(PublicKey ServerPublicKey) {
        this.serverPublicKey = ServerPublicKey;
    }

    public PublicKey getServerPublicKey() {
        return this.serverPublicKey;
    }  

    public void read(ByteBuffer buf) {
        serverPublicKey = ByteBufferAddon.readPublicKey(buf);
    }

    public void write(ByteBuffer buf) {
        ByteBufferAddon.writeKey(buf, serverPublicKey);
    }
    
}
