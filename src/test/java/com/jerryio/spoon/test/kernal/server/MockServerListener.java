package com.jerryio.spoon.test.kernal.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jerryio.spoon.kernal.event.EventHandler;
import com.jerryio.spoon.kernal.event.server.ClientConnectedEvent;
import com.jerryio.spoon.kernal.event.server.ClientDisconnectedEvent;
import com.jerryio.spoon.kernal.network.protocol.general.SendTextPacket;
import com.jerryio.spoon.kernal.network.protocol.handshake.EncryptionBeginPacket;
import com.jerryio.spoon.kernal.server.RemoteDevice;
import com.jerryio.spoon.kernal.server.ServerListener;

public class MockServerListener extends ServerListener {
    public List<Object> log;

    public MockServerListener() {
        this.log = new ArrayList<>();
    }

    public MockServerListener(List<Object> log) {
        this.log = log;
    }

    @EventHandler
    public void logClientAESKey(RemoteDevice device, EncryptionBeginPacket packet) {
        log.add(packet);
    }

    @Override
    @EventHandler
    public void handleTextReceived(RemoteDevice device, SendTextPacket packet) throws IOException {
        log.add(packet);
    }

    @Override
    @EventHandler
    public void onClientConnectedEvent(ClientConnectedEvent event) {
        // log.add(event); // unstable test case
    }

    @Override
    @EventHandler
    public void onClientDisconnectedEvent(ClientDisconnectedEvent event) {
        log.add(event);
    }
    
}
