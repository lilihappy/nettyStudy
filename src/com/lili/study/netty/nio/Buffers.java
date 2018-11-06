package com.lili.study.netty.nio;

import java.nio.ByteBuffer;

public class Buffers {

    public ByteBuffer readByteBuffer;
    
    public ByteBuffer writeByteBuffer;

    public Buffers(int readCapacity, int writeCapacity) {
        this.readByteBuffer = ByteBuffer.allocate(readCapacity);
        this.writeByteBuffer = ByteBuffer.allocate(writeCapacity);
    }

    public ByteBuffer getReadByteBuffer() {
        return readByteBuffer;
    }

    public ByteBuffer getWriteByteBuffer() {
        return writeByteBuffer;
    }
}
