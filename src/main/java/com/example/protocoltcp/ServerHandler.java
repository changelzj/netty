package com.example.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

public class ServerHandler extends SimpleChannelInboundHandler<Protocol> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocol msg) throws Exception {
        System.out.println(msg.getLen());
        System.out.println(new String(msg.getContent(), CharsetUtil.UTF_8));
        System.out.println("***************");

        byte[] resbyte = UUID.randomUUID().toString().getBytes(CharsetUtil.UTF_8);
        Protocol result = new Protocol();
        result.setContent(resbyte);
        result.setLen(resbyte.length);
        ctx.writeAndFlush(result);
    }
}
