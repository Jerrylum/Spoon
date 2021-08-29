package com.jerryio.spoon.kernal.router;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.jerryio.spoon.kernal.event.EventListener;
import com.jerryio.spoon.kernal.event.EventListenerManager;
import com.jerryio.spoon.kernal.event.server.ClientConnectedEvent;
import com.jerryio.spoon.kernal.event.server.ClientDisconnectedEvent;
import com.jerryio.spoon.kernal.event.server.ServerErrorEvent;
import com.jerryio.spoon.kernal.network.PacketEncoder;
import com.jerryio.spoon.kernal.network.protocol.Packet;
import com.jerryio.spoon.kernal.network.security.EncryptionManager;
import com.jerryio.spoon.kernal.network.security.RSAUtils;
import com.jerryio.spoon.kernal.server.RemoteDevice;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;


public class RouterDevice extends WebSocketServer implements EventListenerManager {
    private Set<EventListener> allListeners;
    private Map<WebSocket, RemoteDevice> allDevices;
    private KeyPair key;
    private EncryptionManager encryption;

    public RouterDevice(InetSocketAddress address, RouterListener listener) throws GeneralSecurityException {
        super(address);
        allListeners = new HashSet<>();
        allDevices = new HashMap<WebSocket, RemoteDevice>();
        key = RSAUtils.createKeys(2048);
        encryption = new EncryptionManager();
        encryption.makeRSAPrivateDecryptCipher((RSAPrivateKey) key.getPrivate());

        listener.setRouter(this);
        this.addEventListener(listener);
    }

    public KeyPair getKey() {
        return this.key;
    }

    public EncryptionManager getEncryption() {
        return this.encryption;
    }

    public Collection<RemoteDevice> getDevices() {
        return this.allDevices.values();
    }
    
    @Override
    public Set<EventListener> getEventListeners() {
        return this.allListeners;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        RemoteDevice device = new RemoteDevice(conn);
        allDevices.put(conn, device);
        fireEvent(new ClientConnectedEvent(device, handshake));
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        RemoteDevice device = allDevices.getOrDefault(conn, new RemoteDevice(conn));
        allDevices.remove(conn);
        fireEvent(new ClientDisconnectedEvent(device, code, reason, remote));
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer buffer) {
        try {
            RemoteDevice device = allDevices.get(conn);
            Packet packet = PacketEncoder.decode(buffer, device.getConnection());

            fireEvent(device, packet);
        } catch (Exception e) {
            onError(conn, e);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        // ex.printStackTrace();
        fireEvent(new ServerErrorEvent(conn, ex));
    }

    @Override
    public void onStart() {
        System.out.println("Router started");
        this.setConnectionLostTimeout(100);
    }

}
