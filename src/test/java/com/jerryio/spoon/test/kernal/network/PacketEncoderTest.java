package com.jerryio.spoon.test.kernal.network;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.jerryio.spoon.kernal.network.PacketEncoder;
import com.jerryio.spoon.kernal.network.protocol.general.SetChannelPacket;
import com.jerryio.spoon.kernal.network.security.AESUtils;

import org.junit.Test;

public class PacketEncoderTest {

    @Test
    public void testPacketEncoderContructer() throws Exception {
        new PacketEncoder();
    }

    @Test
    public void testEncodeDecodeRawPacket() throws Exception {
        MockConnection conn = new MockConnection();

        SetChannelPacket expected = new SetChannelPacket("hello world");
        ByteBuffer buffer = PacketEncoder.encode(expected, conn);
        
        SetChannelPacket actual = (SetChannelPacket)PacketEncoder.decode(buffer, conn);

        assertEquals(expected.getChannel(), actual.getChannel());
    }

    @Test
    public void testEncodeDecodeEncryptedPacket() throws Exception {
        MockConnection conn = new MockConnection();

        SecretKey key = AESUtils.createKey(256);
        IvParameterSpec iv = AESUtils.createIv(16);

        Cipher AESEncryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");
        AESEncryptCipher.init(Cipher.ENCRYPT_MODE, key, iv);
        Cipher AESDecryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");
        AESDecryptCipher.init(Cipher.DECRYPT_MODE, key, iv);

        conn.getEncryption().makeAESCiphers(key, iv);

        SetChannelPacket expected = new SetChannelPacket("hello world");
        ByteBuffer buffer = PacketEncoder.encode(expected, conn);
        
        SetChannelPacket actual = (SetChannelPacket)PacketEncoder.decode(buffer, conn);

        assertEquals(expected.getChannel(), actual.getChannel());
    }

    @Test(expected = RuntimeException.class)
    public void testRepeatedIdPacket() throws Exception {
        MockConnection conn = new MockConnection();
        conn.sentPackets = conn.receivedPackets;

        SetChannelPacket expected = new SetChannelPacket("hello world");
        ByteBuffer buffer = PacketEncoder.encode(expected, conn);
        
        SetChannelPacket actual = (SetChannelPacket)PacketEncoder.decode(buffer, conn);

        assertEquals(expected.getChannel(), actual.getChannel());
    }
}
