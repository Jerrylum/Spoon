package com.jerryio.spoon.kernal.network.protocol.handshake;

import java.nio.ByteBuffer;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.jerryio.spoon.kernal.network.ByteBufferAddon;

public class EncryptionBeginPacket implements HandshakePacket {

    private SecretKey clientAESKey;
    private IvParameterSpec clientIv;

    public EncryptionBeginPacket() {

    }

    public EncryptionBeginPacket(SecretKey clientAESKey, IvParameterSpec clientIv) {
        this.clientAESKey = clientAESKey;
        this.clientIv = clientIv;
    }

    public SecretKey getClientAESKey() {
        return this.clientAESKey;
    }

    public IvParameterSpec getClientIv() {
        return this.clientIv;
    }

    public void read(ByteBuffer buf) {
        clientAESKey = ByteBufferAddon.readSecretKey(buf);
        byte[] iv_byte = new byte[16];
		buf.get(iv_byte);
		clientIv = new IvParameterSpec(iv_byte);
    }

    public void write(ByteBuffer buf) {
        ByteBufferAddon.writeKey(buf, clientAESKey);
        buf.put(clientIv.getIV());
    }

}
