package com.jerryio.spoon.kernal.client;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import com.jerryio.spoon.kernal.event.EventListener;
import com.jerryio.spoon.kernal.event.EventListenerManager;
import com.jerryio.spoon.kernal.event.client.ConnectionCloseEvent;
import com.jerryio.spoon.kernal.event.client.ConnectionOpenEvent;
import com.jerryio.spoon.kernal.event.client.ClientErrorEvent;
import com.jerryio.spoon.kernal.network.Connection;
import com.jerryio.spoon.kernal.network.PacketEncoder;
import com.jerryio.spoon.kernal.network.protocol.Packet;
import com.jerryio.spoon.kernal.network.security.EncryptionManager;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class ClientConnection extends WebSocketClient implements Connection, EventListenerManager {

    private Set<EventListener> allListeners;
    private EncryptionManager encryption;
    private int receivedPackets = 0;
    private int sentPackets = 1;

    public ClientConnection(URI serverUri) {
        super(serverUri);
        allListeners = new HashSet<>();
        encryption = new EncryptionManager();
    }

    @Override
    public WebSocket getWebSocket() {
        return this;
    }
    
    @Override
    public int getReceivedPackets() {
        return receivedPackets;
    }

    @Override
    public void setReceivedPackets(int num) {
        this.receivedPackets = num;
    }

    @Override
    public int getSentPackets() {
        return sentPackets;
    }

    @Override
    public void setSentPackets(int num) {
        this.sentPackets = num;
    }

    @Override
    public boolean isConnected() {
        return getWebSocket().isOpen();
    }

    @Override
    public Set<EventListener> getEventListeners() {
        return allListeners;
    }

    @Override
    public EncryptionManager getEncryption() {
        return encryption;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        fireEvent(new ConnectionOpenEvent(handshakedata));
    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onMessage(ByteBuffer buffer) {
        try {
            Packet packet = PacketEncoder.decode(buffer, this);

            fireEvent(packet);
        } catch (Exception e) {
            onError(e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        fireEvent(new ConnectionCloseEvent(code, reason, remote));
    }

    @Override
    public void onError(Exception ex) {
        fireEvent(new ClientErrorEvent(ex));
    }
    
}
