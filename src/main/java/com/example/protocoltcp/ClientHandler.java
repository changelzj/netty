package com.example.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ClientHandler extends SimpleChannelInboundHandler<Protocol> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Protocol msg) throws Exception {
        System.out.println(new String(msg.getContent(), CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            String s = "今天天气不错 " + i;
            byte[] bytes = s.getBytes(CharsetUtil.UTF_8);
            Protocol protocol = new Protocol();
            protocol.setContent(bytes);
            protocol.setLen(bytes.length);
            ctx.writeAndFlush(protocol);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getLocalizedMessage());
    }
}
