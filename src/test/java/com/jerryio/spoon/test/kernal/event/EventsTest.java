package com.jerryio.spoon.test.kernal.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.jerryio.spoon.kernal.event.client.ClientErrorEvent;
import com.jerryio.spoon.kernal.event.client.ConnectionCloseEvent;
import com.jerryio.spoon.kernal.event.client.ConnectionOpenEvent;
import com.jerryio.spoon.kernal.event.server.ClientConnectedEvent;
import com.jerryio.spoon.kernal.event.server.ClientDisconnectedEvent;
import com.jerryio.spoon.kernal.event.server.ServerErrorEvent;
import com.jerryio.spoon.kernal.server.RemoteDevice;

import org.junit.Test;

public class EventsTest {
    @Test
    public void testClientErrorEvent() {
        Exception ex = new Exception("hello world");

        assertEquals(ex, new ClientErrorEvent(ex).getException());
    }

    @Test
    public void testConnectionCloseEvent() {
        ConnectionCloseEvent event = new ConnectionCloseEvent(3, "hello", false);

        assertEquals(3, event.getCode());
        assertEquals("hello", event.getReason());
        assertEquals(false, event.isRemote());
    }

    @Test
    public void testConnectionOpenEvent() {
        ConnectionOpenEvent event = new ConnectionOpenEvent(null);

        assertEquals(null, event.getHandshakedata());
    }

    @Test
    public void testClientConnectedEvent() {
        RemoteDevice device = new RemoteDevice(3, null);
        ClientConnectedEvent event = new ClientConnectedEvent(device, null);

        assertEquals(device, event.getDevice());
        assertEquals(null, event.getHandshakedata());
    }

    @Test
    public void testClientDisconnectedEvent() {
        RemoteDevice device = new RemoteDevice(3, null);
        ClientDisconnectedEvent event = new ClientDisconnectedEvent(device, 3, "hello", false);

        assertEquals(device, event.getDevice());
        assertEquals(3, event.getCode());
        assertEquals("hello", event.getReason());
        assertEquals(false, event.isRemote());
    }

    @Test
    public void testServerErrorEvent() {
        Exception ex = new Exception("hello world");
        ServerErrorEvent event = new ServerErrorEvent(null, ex);

        assertEquals(ex, event.getException());
        assertEquals(null, event.getSocket());
        assertFalse(event.isSocketProblem());
    }
}
