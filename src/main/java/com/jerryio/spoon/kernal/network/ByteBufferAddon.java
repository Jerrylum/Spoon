package com.jerryio.spoon.kernal.network;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.jerryio.spoon.kernal.network.security.AESUtils;
import com.jerryio.spoon.kernal.network.security.RSAUtils;

public abstract class ByteBufferAddon {

    public static String readUtf(ByteBuffer buffer) {
        return readUtf(buffer, 32767);
    }

    public static String readUtf(ByteBuffer buffer, int maxStringLength) {
        int actualByteSize = buffer.getInt();

        if (actualByteSize > maxStringLength * 4)
            throw new RuntimeException("The received encoded string buffer length is longer than maximum allowed (" + actualByteSize + " > " + maxStringLength * 4 + ")");
        if (actualByteSize < 0)
            throw new RuntimeException("The received encoded string buffer length is less than zero! Weird string!");

        byte[] buf2 = new byte[actualByteSize];
        buffer.get(buf2);
        String string = new String(buf2, StandardCharsets.UTF_8);

        return string;
    }

    public static void writeUtf(ByteBuffer buffer, String string) {
        writeUtf(buffer, string, 32767*4);
    }

    public static void writeUtf(ByteBuffer buffer, String string, int maxByteSize) {
        byte[] arrby = string.getBytes(StandardCharsets.UTF_8);
        if (arrby.length > maxByteSize) {
            throw new RuntimeException("String too big (was " + arrby.length + " bytes encoded, max " + maxByteSize + ")");
        }
        buffer.putInt(arrby.length).put(arrby);
    }

    public static PublicKey readPublicKey(ByteBuffer buffer) {
        int actualByteSize = buffer.getInt();

        if (actualByteSize > 512)
            throw new RuntimeException("The received encoded buffer length is longer than maximum allowed (" + actualByteSize + ")");
        if (actualByteSize < 0)
            throw new RuntimeException("The received encoded buffer length is less than zero! Weird data!");

        byte[] buf2 = new byte[actualByteSize];
        buffer.get(buf2);

        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(buf2);
            KeyFactory kf = KeyFactory.getInstance(RSAUtils.RSA_ALGORITHM);
            return kf.generatePublic(spec);
        } catch(Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static SecretKey readSecretKey(ByteBuffer buffer) {
        int actualByteSize = buffer.getInt();

        if (actualByteSize > 64)
            throw new RuntimeException("The received encoded buffer length is longer than maximum allowed (" + actualByteSize + ")");
        if (actualByteSize < 0)
            throw new RuntimeException("The received encoded buffer length is less than zero! Weird data!");

        byte[] buf2 = new byte[actualByteSize];
        buffer.get(buf2);

        return new SecretKeySpec(buf2, 0, buf2.length, AESUtils.AES_ALGORITHM);
    }

    public static void writeKey(ByteBuffer buffer, Key key) {
        byte[] payload = key.getEncoded();
        buffer.putInt(payload.length).put(payload);
    }
}
