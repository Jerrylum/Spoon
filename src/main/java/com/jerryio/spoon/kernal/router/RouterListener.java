package com.jerryio.spoon.kernal.router;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.jerryio.spoon.kernal.event.EventHandler;
import com.jerryio.spoon.kernal.event.EventListener;
import com.jerryio.spoon.kernal.event.server.ClientConnectedEvent;
import com.jerryio.spoon.kernal.event.server.ClientDisconnectedEvent;
import com.jerryio.spoon.kernal.network.protocol.general.SendTextPacket;
import com.jerryio.spoon.kernal.network.protocol.general.SetChannelPacket;
import com.jerryio.spoon.kernal.network.protocol.handshake.EncryptionBeginPacket;
import com.jerryio.spoon.kernal.network.protocol.handshake.RequireEncryptionPacket;
import com.jerryio.spoon.kernal.network.protocol.reply.ReceivedPacket;
import com.jerryio.spoon.kernal.server.RemoteDevice;

public abstract class RouterListener implements EventListener {

    protected RouterDevice router;

    protected void setRouter(RouterDevice router) {
        this.router = router;
    }

    public RouterDevice getRouter() {
        return this.router;
    }

    @EventHandler
    public void handleClientConnected(ClientConnectedEvent event) throws IOException {
        event.getDevice().getConnection().getEncryption().setRSADecryptCipher(getRouter().getEncryption().getRSADecryptCipher());
        event.getDevice().getConnection().sendPacket(new RequireEncryptionPacket(getRouter().getKey().getPublic()));
    }

    @EventHandler
    public void handleClientAESkey(RemoteDevice device, EncryptionBeginPacket packet) throws GeneralSecurityException {
        device.getConnection().getEncryption().makeAESCiphers(packet.getClientAESKey(), packet.getClientIv());
    }

    @EventHandler
    public void handleTextReceived(RemoteDevice device, SendTextPacket packet) throws IOException {
        // Confirm
        device.getConnection().sendPacket(new ReceivedPacket(device.getConnection().getReceivedPackets()));

        // Broadcast
        for (RemoteDevice r : getRouter().getDevices())
            if (r.getChannel().equals(device.getChannel()) && !r.equals(device))
                r.sendTextMessage(packet.getMessage());
    }

    @EventHandler
    public void handleSetChannel(RemoteDevice device, SetChannelPacket packet) {
        device.setChannel(packet.getChannel());
    }

    public abstract void onClientConnectedEvent(ClientConnectedEvent event);

    public abstract void onClientDisconnectedEvent(ClientDisconnectedEvent event);

}
