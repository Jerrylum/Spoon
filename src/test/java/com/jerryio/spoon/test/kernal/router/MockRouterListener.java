package com.jerryio.spoon.test.kernal.router;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jerryio.spoon.kernal.event.EventHandler;
import com.jerryio.spoon.kernal.event.server.ClientConnectedEvent;
import com.jerryio.spoon.kernal.event.server.ClientDisconnectedEvent;
import com.jerryio.spoon.kernal.event.server.ServerErrorEvent;
import com.jerryio.spoon.kernal.network.protocol.general.SendTextPacket;
import com.jerryio.spoon.kernal.network.protocol.general.SetChannelPacket;
import com.jerryio.spoon.kernal.network.protocol.handshake.EncryptionBeginPacket;
import com.jerryio.spoon.kernal.server.RemoteDevice;
import com.jerryio.spoon.kernal.router.RouterListener;

public class MockRouterListener extends RouterListener {
    public List<Object> log;

    public MockRouterListener() {
        this.log = new ArrayList<>();
    }

    public MockRouterListener(List<Object> log) {
        this.log = log;
    }

    @EventHandler
    public void logClientAESKey(RemoteDevice device, EncryptionBeginPacket packet) {
        log.add(packet);
    }

    @EventHandler
    public void logClientChangeChange(RemoteDevice device, SetChannelPacket packet) {
        log.add(packet);
    }

    @EventHandler
    public void logTextReceived(RemoteDevice device, SendTextPacket packet) throws IOException {
        log.add(packet);
    }

    @EventHandler
    public void logServerError(ServerErrorEvent event) {
        log.add(event);
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
