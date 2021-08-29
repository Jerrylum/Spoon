package com.jerryio.spoon.kernal.client;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.jerryio.spoon.kernal.event.EventHandler;
import com.jerryio.spoon.kernal.event.EventListener;
import com.jerryio.spoon.kernal.event.client.ConnectionCloseEvent;
import com.jerryio.spoon.kernal.event.client.ConnectionOpenEvent;
import com.jerryio.spoon.kernal.network.protocol.general.SendTextPacket;
import com.jerryio.spoon.kernal.network.protocol.handshake.EncryptionBeginPacket;
import com.jerryio.spoon.kernal.network.protocol.handshake.RequireEncryptionPacket;
import com.jerryio.spoon.kernal.network.protocol.reply.ReceivedPacket;
import com.jerryio.spoon.kernal.network.security.AESUtils;

public abstract class ClientListener implements EventListener {
    
    protected ClientDevice client;

    protected void setClient(ClientDevice client) {
        this.client = client;
    }

    public ClientDevice getClient() {
        return this.client;
    }

    @EventHandler
    public void handleRequireEncryption(RequireEncryptionPacket packet) throws GeneralSecurityException, IOException {
        if (getClient().isEncrypted()) return;

        ClientConnection conn = getClient().getConnection();
        
        conn.getEncryption().makeRSAPublicEncryptCipher((RSAPublicKey)packet.getServerPublicKey());

        SecretKey key = AESUtils.createKey(256);
        IvParameterSpec iv = AESUtils.createIv(16);

        conn.sendPacket(new EncryptionBeginPacket(key, iv));

        conn.getEncryption().makeAESCiphers(key, iv);
    }

    @EventHandler
    public abstract void handleTextReceived(SendTextPacket packet) throws IOException;

    @EventHandler
    public void handlePacketSent(ReceivedPacket packet) {
        if (getClient().getLastMessagePacketId() == packet.getReceivedPacketId())
            this.lastMessageReceivedEvent();
    }

    public abstract void lastMessageReceivedEvent();

    public abstract void onOpenEvent(ConnectionOpenEvent event);

    public abstract void onCloseEvent(ConnectionCloseEvent event);
}
