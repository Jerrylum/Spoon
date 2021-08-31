package com.jerryio.spoon.test.kernal.network;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.PublicKey;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.jerryio.spoon.kernal.network.ByteBufferAddon;
import com.jerryio.spoon.kernal.network.security.AESUtils;
import com.jerryio.spoon.kernal.network.security.RSAUtils;

import org.junit.Test;

public class ByteBufferAddonTest {

	@Test
    public void testByteBufferAddon() throws Exception {
        new ByteBufferAddon() {};
    }

	@Test
	public void testReadWriteAsciiString() {
		String expected = "hello world";

		ByteBuffer buffer = ByteBuffer.allocate(1024);
		ByteBufferAddon.writeUtf(buffer, expected);

		buffer.rewind();
		String actual = ByteBufferAddon.readUtf(buffer);

		assertEquals(expected, actual);
	}

	@Test
	public void testReadWriteUtfString() {
		String expected = "你今晚去邊度食飯？";

		ByteBuffer buffer = ByteBuffer.allocate(1024);
		ByteBufferAddon.writeUtf(buffer, expected);

		buffer.rewind();
		String actual = ByteBufferAddon.readUtf(buffer);

		assertEquals(expected, actual);
	}

	@Test(expected = RuntimeException.class)
	public void testWriteOversizeString() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);

		ByteBufferAddon.writeUtf(buffer, "hello world", 5);
	}

	@Test(expected = RuntimeException.class)
	public void testReadOversizeString() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		ByteBufferAddon.writeUtf(buffer, "hello world");

		buffer.rewind();
		ByteBufferAddon.readUtf(buffer, 1);
	}
	
	@Test(expected = RuntimeException.class)
	public void testReadLengthNegativeString() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.putInt(-1);

		buffer.rewind();
		ByteBufferAddon.readUtf(buffer);
	}

	@Test
	public void testReadWritePublicKey() {
		KeyPair keyPair = RSAUtils.createKeys(1024);
		PublicKey expected = keyPair.getPublic();

		ByteBuffer buffer = ByteBuffer.allocate(1024);
		ByteBufferAddon.writeKey(buffer, expected);

		buffer.rewind();
		PublicKey actual = ByteBufferAddon.readPublicKey(buffer);

		assertArrayEquals(expected.getEncoded(), actual.getEncoded());
	}
	
	@Test(expected = RuntimeException.class)
	public void testReadOversizePublicKey() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.putInt(513);

		buffer.rewind();
		ByteBufferAddon.readPublicKey(buffer);
	}
	
	@Test(expected = RuntimeException.class)
	public void testReadLengthNegativePublicKey() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.putInt(-1);

		buffer.rewind();
		ByteBufferAddon.readPublicKey(buffer);
	}

	@Test
	public void testReadWriteSecretKey() {
		SecretKey expected = AESUtils.createKey(256);

		ByteBuffer buffer = ByteBuffer.allocate(1024);
		ByteBufferAddon.writeKey(buffer, expected);

		buffer.rewind();
		SecretKey actual = ByteBufferAddon.readSecretKey(buffer);

		assertArrayEquals(expected.getEncoded(), actual.getEncoded());
	}
		
	@Test(expected = RuntimeException.class)
	public void testReadOversizeSecretKey() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.putInt(65);

		buffer.rewind();
		ByteBufferAddon.readSecretKey(buffer);
	}
	
	@Test(expected = RuntimeException.class)
	public void testReadLengthNegativeSecretKey() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.putInt(-1);

		buffer.rewind();
		ByteBufferAddon.readSecretKey(buffer);
	}

	@Test
	public void testReadWriteIV() {
		IvParameterSpec expected = AESUtils.createIv(16);

		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put(expected.getIV());

		buffer.rewind();
		byte[] iv_byte = new byte[16];
		buffer.get(iv_byte);
		IvParameterSpec actual = new IvParameterSpec(iv_byte);

		assertArrayEquals(expected.getIV(), actual.getIV());
	}
}
