package com.jerryio.spoon.kernal.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;

import com.jerryio.spoon.kernal.ProviderDevice;
import com.jerryio.spoon.kernal.network.protocol.Packet;
import com.jerryio.spoon.kernal.network.protocol.general.SendTextPacket;

public class ServerDevice extends ProviderDevice {

    public ServerDevice(InetSocketAddress address, ServerListener listener) throws GeneralSecurityException {
        super(address);

        listener.setServer(this);
        this.addEventListener(listener);
    }

    public void sendTextMessage(String msg) throws IOException {
        Packet packet = new SendTextPacket(msg);
        for (RemoteDevice remote : allDevices.values()) {
            remote.getConnection().sendPacket(packet);
        }
    }
}
