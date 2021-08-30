package com.jerryio.spoon.test.kernal.router;

import static org.junit.Assert.*;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.jerryio.spoon.kernal.client.ClientDevice;
import com.jerryio.spoon.kernal.router.RouterDevice;
import com.jerryio.spoon.test.kernal.client.MockClientListener;

import org.junit.Test;

public class RouterDeviceTest {
    private static int port = 8000;

    @Test
    public void testRouterStartup() throws Exception {
        RouterDevice device =  new RouterDevice(new InetSocketAddress(++port), new MockRouterListener());

        device.start();

        device.stop();
    }

    @Test
    public void testClientConnectRouter() throws Exception {
        RouterDevice router =  new RouterDevice(new InetSocketAddress(++port), new MockRouterListener());
        router.start();

        Thread.sleep(200);

        ClientDevice localClient = new ClientDevice(new URI("ws://127.0.0.1:" + port), new MockClientListener());

        Thread.sleep(200);

        assertTrue(localClient.getConnection().isConnected());
    }

    @Test
    public void testConnectionEncryption() throws Exception {
        RouterDevice router =  new RouterDevice(new InetSocketAddress(++port), new MockRouterListener());
        router.start();

        Thread.sleep(200);

        ClientDevice localClient = new ClientDevice(new URI("ws://127.0.0.1:" + port), new MockClientListener());

        Thread.sleep(200);

        assertTrue(localClient.isEncrypted());
    }

    @Test
    public void testConnectionEvents() throws Exception {
        List<Object> events = new ArrayList<>();

        RouterDevice router =  new RouterDevice(new InetSocketAddress(++port), new MockRouterListener(events));
        router.start();

        Thread.sleep(200);

        ClientDevice localClient1 = new ClientDevice(new URI("ws://127.0.0.1:" + port), new MockClientListener(events));

        Thread.sleep(200);

        ClientDevice localClient2 = new ClientDevice(new URI("ws://127.0.0.1:" + port), new MockClientListener(events));

        Thread.sleep(200);

        ClientDevice localClient3 = new ClientDevice(new URI("ws://127.0.0.1:" + port), new MockClientListener(events));

        Thread.sleep(200);

        localClient1.setChannel("Apple");
        localClient2.setChannel("Apple");
        localClient3.setChannel("Orange");

        Thread.sleep(50);

        localClient1.sendTextMessage("hello A");

        Thread.sleep(50);

        localClient2.sendTextMessage("hello B");

        Thread.sleep(50);

        localClient3.sendTextMessage("hello C");

        Thread.sleep(50);
        
        localClient2.setChannel("Orange");

        Thread.sleep(50);

        localClient2.sendTextMessage("hello D");

        Thread.sleep(50);

        localClient1.getConnection().close();
        localClient2.getConnection().close();
        localClient3.getConnection().close();

        Thread.sleep(50);

        assertFalse(localClient1.getConnection().isConnected());

        assertEquals(27, events.size());
    }
}
