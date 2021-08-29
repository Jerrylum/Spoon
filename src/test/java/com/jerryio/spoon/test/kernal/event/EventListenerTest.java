package com.jerryio.spoon.test.kernal.event;

import static org.junit.Assert.*;

import java.util.UUID;

import com.jerryio.spoon.kernal.event.EventHandler;
import com.jerryio.spoon.kernal.event.EventListener;

import org.junit.Test;

public class EventListenerTest {
    private static UUID testFlag = UUID.randomUUID();

    @Test
    public void testFireEvent() {
        MockEventListenerManager manager = new MockEventListenerManager();
        manager.addEventListener(new MockEventListener());

        UUID expected = UUID.randomUUID();

        manager.fireEvent(expected);

        assertEquals(expected, testFlag);
    }

    @Test
    public void testManipulateEventListener() {
        MockEventListenerManager manager = new MockEventListenerManager();

        manager.addEventListener(new MockEventListener());
        assertEquals(1, manager.getEventListeners().size());

        manager.addEventListener(new MockEventListener());
        assertEquals(2, manager.getEventListeners().size());

        manager.removeEventListener(new MockEventListener());
        assertEquals(2, manager.getEventListeners().size());

        manager.removeEventListener(MockEventListener.class);
        assertEquals(0, manager.getEventListeners().size());

        MockEventListener listener = new MockEventListener();

        manager.addEventListener(listener);
        assertEquals(1, manager.getEventListeners().size());

        manager.addEventListener(listener);
        assertEquals(1, manager.getEventListeners().size());

        manager.removeEventListener(listener);
        assertEquals(0, manager.getEventListeners().size());
    }

    public static class MockEventListener implements EventListener {
        @EventHandler
        public void testEvent(UUID id) {
            testFlag = id;
        }

        public void testNotEvent(UUID id) {
            throw new RuntimeException("test failed");
        }

        @EventHandler
        public void testEvent(Integer somethingElse) {
            throw new RuntimeException("test failed");
        }
    }
}
