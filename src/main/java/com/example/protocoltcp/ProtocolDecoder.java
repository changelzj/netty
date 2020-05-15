package com.example.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class ProtocolDecoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("decode");
        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        Protocol protocol = new Protocol();
        protocol.setLen(len);
        protocol.setContent(bytes);
        out.add(protocol);
    }
}
