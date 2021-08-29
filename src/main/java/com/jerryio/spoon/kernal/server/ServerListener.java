package com.jerryio.spoon.kernal.server;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.jerryio.spoon.kernal.event.EventHandler;
import com.jerryio.spoon.kernal.event.EventListener;
import com.jerryio.spoon.kernal.event.server.ClientConnectedEvent;
import com.jerryio.spoon.kernal.event.server.ClientDisconnectedEvent;
import com.jerryio.spoon.kernal.network.protocol.general.SendTextPacket;
import com.jerryio.spoon.kernal.network.protocol.handshake.EncryptionBeginPacket;
import com.jerryio.spoon.kernal.network.protocol.handshake.RequireEncryptionPacket;
import com.jerryio.spoon.kernal.network.protocol.reply.ReceivedPacket;

public abstract class ServerListener implements EventListener {

    protected ServerDevice server;

    protected void setServer(ServerDevice server) {
        this.server = server;
    }

    public ServerDevice getServer() {
        return this.server;
    }

    @EventHandler
    public void handleClientConnected(ClientConnectedEvent event) throws IOException {
        event.getDevice().getConnection().getEncryption().setRSADecryptCipher(getServer().getEncryption().getRSADecryptCipher());
        event.getDevice().getConnection().sendPacket(new RequireEncryptionPacket(getServer().getKey().getPublic()));
    }

    @EventHandler
    public void handleClientAESkey(RemoteDevice device, EncryptionBeginPacket packet) throws GeneralSecurityException {
        device.getConnection().getEncryption().makeAESCiphers(packet.getClientAESKey(), packet.getClientIv());
    }

    @EventHandler
    public void handleTextReceivedAndConfirm(RemoteDevice device, SendTextPacket packet) throws IOException {
        device.getConnection().sendPacket(new ReceivedPacket(device.getConnection().getReceivedPackets()));
    }

    public abstract void handleTextReceived(RemoteDevice device, SendTextPacket packet) throws IOException;
    
    public abstract void onClientConnectedEvent(ClientConnectedEvent event);

    public abstract void onClientDisconnectedEvent(ClientDisconnectedEvent event);

}
