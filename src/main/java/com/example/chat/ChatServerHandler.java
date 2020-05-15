package com.example.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    
    /**
     * 一连接就执行
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String msg = "[用户] " + channel.remoteAddress() + " 上线了";
        System.out.println(msg);
        // 广播发送，channelGroup中遍历发送消息
        channelGroup.writeAndFlush(msg);
        channelGroup.add(channel);
    }

    

    /**
     * 连接断开，断开时不需要手动从channelGroup移除
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String msg = "[用户] " + channel.remoteAddress() + " 离线了";
        System.out.println(msg);
        channelGroup.writeAndFlush(msg);
        
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(e -> {
            if (e != channel) {
                e.writeAndFlush("[用户] " + channel.remoteAddress() + " 发送了: " + msg);
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
