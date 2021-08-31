package com.jerryio.spoon.kernal.event;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Iterator;
import java.util.Set;

public interface EventListenerManager {
    public Set<EventListener> getEventListeners();

    default void addEventListener(EventListener listener) {
        getEventListeners().add(listener);
    }

    default void removeEventListener(EventListener listener) {
        getEventListeners().remove(listener);
    }

    default void removeEventListener(Class<? extends EventListener> clazz) {
        Iterator<EventListener> i = getEventListeners().iterator();
        while (i.hasNext()) {
            if (i.next().getClass() == clazz)
                i.remove();
        }
    }

    default boolean fireEvent(Object... args) {
        try {
            Set<EventListener> all = getEventListeners();
            for (EventListener listener : all) {
                methods: for (Method m : listener.getClass().getMethods()) {
                    if (!m.isAnnotationPresent(EventHandler.class)) continue;
                    if (m.getParameterCount() != args.length) continue;

                    Parameter[] parms = m.getParameters();
                    for (int i = 0; i < parms.length; i++) {
                        if (parms[i].getType() != args[i].getClass()) continue methods;
                    }

                    try {
                        m.invoke(listener, args);
                    } catch (Exception e) {
                    }
                }
            }
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
