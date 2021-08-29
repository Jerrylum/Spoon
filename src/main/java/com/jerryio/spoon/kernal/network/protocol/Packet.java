package com.jerryio.spoon.kernal.network.protocol;

import java.nio.ByteBuffer;

public interface Packet {

	public void read(ByteBuffer buf);

    public void write(ByteBuffer buf);
	
}
