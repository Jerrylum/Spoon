package com.jerryio.spoon.test.kernal.server;

import static org.junit.Assert.*;

import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jerryio.spoon.kernal.client.ClientDevice;
import com.jerryio.spoon.kernal.event.client.ClientErrorEvent;
import com.jerryio.spoon.kernal.event.client.ConnectionCloseEvent;
import com.jerryio.spoon.kernal.event.server.ClientDisconnectedEvent;
import com.jerryio.spoon.kernal.event.server.ServerErrorEvent;
import com.jerryio.spoon.kernal.network.protocol.general.SendTextPacket;
import com.jerryio.spoon.kernal.network.protocol.handshake.EncryptionBeginPacket;
import com.jerryio.spoon.kernal.network.protocol.handshake.RequireEncryptionPacket;
import com.jerryio.spoon.kernal.network.protocol.reply.ReceivedPacket;
import com.jerryio.spoon.kernal.server.RemoteConnection;
import com.jerryio.spoon.kernal.server.RemoteDevice;
import com.jerryio.spoon.kernal.server.ServerDevice;
import com.jerryio.spoon.test.kernal.client.MockClientListener;

import org.java_websocket.WebSocket;
import org.junit.Test;

public class ServerDeviceTest {
    private static int port = 7000;

    @Test
    public void testServerStartup() throws Exception {
        ServerDevice device =  new ServerDevice(new InetSocketAddress(++port), new MockServerListener());

        device.start();

        device.stop();
    }

    @Test
    public void testClientConnectServer() throws Exception {
        ServerDevice server =  new ServerDevice(new InetSocketAddress(++port), new MockServerListener());
        server.start();

        Thread.sleep(200);

        ClientDevice localClient = new ClientDevice(new URI("ws://127.0.0.1:" + port), new MockClientListener());

        Thread.sleep(200);

        assertTrue(localClient.getConnection().isConnected());
        assertTrue(server.getDevice(0).getConnection().isConnected());
    }

    @Test
    public void testConnectionEncryption() throws Exception {
        ServerDevice server =  new ServerDevice(new InetSocketAddress(++port), new MockServerListener());
        server.start();

        Thread.sleep(200);

        ClientDevice localClient = new ClientDevice(new URI("ws://127.0.0.1:" + port), new MockClientListener());

        Thread.sleep(200);

        assertTrue(localClient.isEncrypted());
        assertTrue(server.getDevice(0).isEncrypted());

        server.getDevice(0).getConnection().getEncryption().setAESEncryptCipher(null); // just for test
        assertFalse(server.getDevice(0).isEncrypted());
        assertNull(server.getDevice(1));
    }
    
    @Test
    public void testConnectionEvents() throws Exception {
        List<Object> events = new ArrayList<>();
        events.add(null); // placeholder
        events.add(null); // placeholder

        ServerDevice server =  new ServerDevice(new InetSocketAddress(++port), new MockServerListener(events));
        server.start();

        Thread.sleep(200);

        ClientDevice localClient = new ClientDevice(new URI("ws://127.0.0.1:" + port), new MockClientListener(events));

        assertFalse(localClient.isEncrypted());

        Thread.sleep(200);

        assertTrue(localClient.isEncrypted());

        localClient.sendTextMessage("hello server");

        Thread.sleep(200);

        server.sendTextMessage("hello client");

        Thread.sleep(200);

        localClient.sendTextMessage("你好");

        Thread.sleep(200);

        localClient.getConnection().close();

        Thread.sleep(200);

        assertFalse(localClient.getConnection().isConnected());

        assertEquals(11, events.size());
        // assertTrue(events.get(0) instanceof ClientConnectedEvent);  // server event
        // assertTrue(events.get(1) instanceof ConnectionOpenEvent);  // client event
        assertTrue(events.get(2) instanceof RequireEncryptionPacket); // client receive
        assertTrue(events.get(3) instanceof EncryptionBeginPacket); // server receive
        assertEquals(server.getKey().getPublic(), ((RequireEncryptionPacket)events.get(2)).getServerPublicKey());
        assertEquals("hello server", ((SendTextPacket)events.get(4)).getMessage());
        assertEquals(2, ((ReceivedPacket)events.get(5)).getReceivedPacketId());
        assertEquals("hello client", ((SendTextPacket)events.get(6)).getMessage());
        assertEquals("你好", ((SendTextPacket)events.get(7)).getMessage());
        assertEquals(3, ((ReceivedPacket)events.get(8)).getReceivedPacketId());
        assertTrue(events.get(9) instanceof ClientDisconnectedEvent || events.get(9) instanceof ConnectionCloseEvent);
        assertTrue(events.get(10) instanceof ClientDisconnectedEvent || events.get(10) instanceof ConnectionCloseEvent);
    }

    @Test
    public void testServerGetInvalidPacket() throws Exception {
        List<Object> events = new ArrayList<>();

        ServerDevice server =  new ServerDevice(new InetSocketAddress(++port), new MockServerListener(events));
        server.start();

        Thread.sleep(200);

        ClientDevice localClient1 = new ClientDevice(new URI("ws://127.0.0.1:" + port), new MockClientListener(events));

        Thread.sleep(200);

        localClient1.getConnection().send("Hello");

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.putInt(123);

        localClient1.getConnection().send(buffer);

        Thread.sleep(200);

        assertEquals(3, events.size());
        assertTrue(events.get(2) instanceof ServerErrorEvent);
    }

    @Test
    public void testClientInvalidPacket() throws Exception {
        List<Object> events = new ArrayList<>();

        ServerDevice server =  new ServerDevice(new InetSocketAddress(++port), new MockServerListener(events));
        server.start();

        Thread.sleep(200);

        ClientDevice localClient1 = new ClientDevice(new URI("ws://127.0.0.1:" + port), new MockClientListener(events));

        Thread.sleep(200);

        RemoteConnection conn = server.getDevice(0).getConnection();
        WebSocket remoteSocket = conn.getWebSocket();

        remoteSocket.send("Hello");

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.putInt(123);

        remoteSocket.send(buffer);

        Thread.sleep(200);

        conn.sendPacket(new ReceivedPacket(1000));

        Thread.sleep(200);

        assertEquals(4, events.size());
        assertTrue(events.get(2) instanceof ClientErrorEvent);

        assertTrue(localClient1.getConnection().isConnected());
    }

    @Test
    public void testServerSendInvalidHandshakePacket() throws Exception {
        ServerDevice server =  new ServerDevice(new InetSocketAddress(++port), new MockServerListener());
        server.start();

        Thread.sleep(200);

        ClientDevice localClient = new ClientDevice(new URI("ws://127.0.0.1:" + port), new MockClientListener());

        Thread.sleep(200);

        assertTrue(localClient.isEncrypted());
        assertTrue(server.getDevice(0).isEncrypted());

        server.getDevice(0).getConnection().sendPacket(new RequireEncryptionPacket(server.getKey().getPublic()));

        Thread.sleep(200);

        assertTrue(localClient.getConnection().isConnected());
        assertTrue(server.getDevice(0).getConnection().isConnected());
    }

    @Test
    public void testServerSendFailed() throws Exception {
        List<Object> events = new ArrayList<>();

        ServerDevice server =  new ServerDevice(new InetSocketAddress(++port), new MockServerListener(events));
        server.start();

        Thread.sleep(200);

        ClientDevice localClient = new ClientDevice(new URI("ws://127.0.0.1:" + port), new MockClientListener(events));

        Thread.sleep(200);

        RemoteDevice remote = server.getDevice(0);
        assertTrue(localClient.getConnection().isConnected());
        assertTrue(remote.getConnection().isConnected());

        localClient.getConnection().close();

        Thread.sleep(200);

        assertFalse(localClient.getConnection().isConnected());

        try {
            remote.sendTextMessage("hello");
            fail("should failed");
        } catch (Exception e) {
            // ok
        }

        try {
            localClient.sendTextMessage("world");
            fail("should failed");
        } catch (Exception e) {
            // ok
        }

        Thread.sleep(200);

        assertEquals(4, events.size());
    }
}
