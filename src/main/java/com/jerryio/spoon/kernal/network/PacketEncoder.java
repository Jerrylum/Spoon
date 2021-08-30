package com.jerryio.spoon.kernal.network;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;

import com.jerryio.spoon.kernal.network.protocol.Packet;

public class PacketEncoder {
    public static ByteBuffer encode(Packet packet, Connection connection) throws Exception {
        ByteBuffer gen1 = ByteBuffer.allocate(1024*512);
        gen1.putInt(connection.getSentPackets());
        connection.setSentPackets(connection.getSentPackets() + 1);
        ByteBufferAddon.writeUtf(gen1, packet.getClass().getName());
        packet.write(gen1);
        int gen1Length = gen1.position();
        byte[] gen1bytes = new byte[gen1Length];
        gen1.rewind(); // IMPORTANT: no optimize
        gen1.get(gen1bytes, 0, gen1Length);

        ByteBuffer gen2 = ByteBuffer.allocate(1024*1024);
        gen2.put(connection.getEncryption().encode(gen1bytes));
        int gen2Length = gen2.position();
        byte[] gen2bytes = new byte[gen2Length];
        gen2.rewind(); // IMPORTANT: no optimize
        gen2.get(gen2bytes, 0, gen2Length);

        ByteBuffer gen3 = ByteBuffer.allocate(gen2Length+4);
        gen3.putInt(gen2Length).put(gen2bytes);
        gen3.rewind();
        return gen3;
    }

    public static Packet decode(ByteBuffer gen3, Connection connection) throws Exception {
        int gen3Length = gen3.getInt();
        byte[] gen3bytes = new byte[gen3Length];
        gen3.get(gen3bytes, 0, gen3Length);
        
        ByteBuffer gen2 = ByteBuffer.allocate(1024*512);
        gen2.put(connection.getEncryption().decode(gen3bytes));
        gen2.rewind();

        int id = gen2.getInt();
        if (connection.getReceivedPackets() >= id)
            throw new RuntimeException("Repeated or older packet! (" + id + ")");
        connection.setReceivedPackets(id);
        String clazzName = ByteBufferAddon.readUtf(gen2, 256);
        Class<?> clazz = Class.forName(clazzName);
        Constructor<?> ctor = clazz.getConstructor();
        Packet packet = (Packet) ctor.newInstance(new Object[] { });
        packet.read(gen2);

        return packet;
    }
}
