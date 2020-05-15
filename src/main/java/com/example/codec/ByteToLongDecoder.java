package com.example.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ByteToLongDecoder extends ByteToMessageDecoder {
    /**
     * 
     * @param ctx 上下文
     * @param in 入站的bytebuf
     * @param out 解码后的数据传给下一个inbond handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("decode");
        if (in.readableBytes() >= 8) {
            long val = in.readLong();
            out.add(val);
        }
    }
}
