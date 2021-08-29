package com.jerryio.spoon.test.kernal.event;

import java.util.HashSet;
import java.util.Set;

import com.jerryio.spoon.kernal.event.EventListener;
import com.jerryio.spoon.kernal.event.EventListenerManager;

public class MockEventListenerManager implements EventListenerManager {

    private Set<EventListener> allListeners = new HashSet<>();

    @Override
    public Set<EventListener> getEventListeners() {
        return allListeners;
    }
    
}
