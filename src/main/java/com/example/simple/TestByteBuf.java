package com.example.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class TestByteBuf {
    public static void main(String[] args) {
        ByteBuf buf = Unpooled.buffer(10);
        
        for (int i = 0; i < buf.capacity(); i++) {
            buf.writeByte(i);
        }

        // netty 的 buf中不需要flip反转,因为底层维护了readIndex writeIndex
        // 根据索引读取 readIndex不会增加
        for (int i = 0; i < buf.readableBytes(); i++) {
            System.out.println(buf.getByte(i));
        }

        // readByte，会导致索引增加
        int read = buf.readableBytes();
        for (int i = 0; i < read; i++) {
            System.out.println(buf.readByte());
        }
        
        
    }
}
