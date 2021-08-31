package com.jerryio.spoon.test.kernal.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jerryio.spoon.kernal.client.ClientListener;
import com.jerryio.spoon.kernal.event.EventHandler;
import com.jerryio.spoon.kernal.event.client.ClientErrorEvent;
import com.jerryio.spoon.kernal.event.client.ConnectionCloseEvent;
import com.jerryio.spoon.kernal.event.client.ConnectionOpenEvent;
import com.jerryio.spoon.kernal.network.protocol.general.SendTextPacket;
import com.jerryio.spoon.kernal.network.protocol.handshake.RequireEncryptionPacket;
import com.jerryio.spoon.kernal.network.protocol.reply.ReceivedPacket;

public class MockClientListener extends ClientListener {
    public List<Object> log;

    public MockClientListener() {
        this.log = new ArrayList<>();
    }

    public MockClientListener(List<Object> log) {
        this.log = log;
    }

    @EventHandler
    public void logRequireEncryption(RequireEncryptionPacket packet) {
        log.add(packet);
    }

    @Override
    @EventHandler
    public void handleTextReceived(SendTextPacket packet) throws IOException {
        log.add(packet);
    }

    @Override
    public void lastMessageReceivedEvent() {
        
    }

    @EventHandler
    public void logPacketSent(ReceivedPacket packet) {
        log.add(packet);
    }

    @EventHandler
    public void logClientError(ClientErrorEvent event) {
        log.add(event);
    }

    @Override
    @EventHandler
    public void onOpenEvent(ConnectionOpenEvent event) {
        // log.add(event); // unstable test case
    }

    @Override
    @EventHandler
    public void onCloseEvent(ConnectionCloseEvent event) {
        log.add(event);
    }
    
}
