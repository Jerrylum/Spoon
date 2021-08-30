package com.jerryio.spoon.test.kernal.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.jerryio.spoon.kernal.client.ClientDevice;
import com.jerryio.spoon.kernal.event.client.ConnectionCloseEvent;
import com.jerryio.spoon.kernal.event.server.ClientDisconnectedEvent;
import com.jerryio.spoon.kernal.network.protocol.general.SendTextPacket;
import com.jerryio.spoon.kernal.network.protocol.handshake.EncryptionBeginPacket;
import com.jerryio.spoon.kernal.network.protocol.handshake.RequireEncryptionPacket;
import com.jerryio.spoon.kernal.network.protocol.reply.ReceivedPacket;
import com.jerryio.spoon.kernal.server.ServerDevice;
import com.jerryio.spoon.test.kernal.client.MockClientListener;

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
    }

    @Test
    public void testConnectionEncryption() throws Exception {
        ServerDevice server =  new ServerDevice(new InetSocketAddress(++port), new MockServerListener());
        server.start();

        Thread.sleep(200);

        ClientDevice localClient = new ClientDevice(new URI("ws://127.0.0.1:" + port), new MockClientListener());

        Thread.sleep(200);

        assertTrue(localClient.isEncrypted());
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

        Thread.sleep(200);

        localClient.sendTextMessage("hello server");

        Thread.sleep(200);

        server.sendTextMessage("hello client");

        Thread.sleep(200);

        localClient.sendTextMessage("你好");

        Thread.sleep(200);
        
        assertTrue(localClient.isEncrypted());

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
}
