package com.jerryio.spoon.kernal.router;

import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;

import com.jerryio.spoon.kernal.ProviderDevice;


public class RouterDevice extends ProviderDevice {

    public RouterDevice(InetSocketAddress address, RouterListener listener) throws GeneralSecurityException {
        super(address);

        listener.setRouter(this);
        this.addEventListener(listener);
    }
}
