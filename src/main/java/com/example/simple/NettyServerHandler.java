package com.example.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * @param ctx 上下文，包含管道，通道等
     * @param msg 客户端传来的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(ctx);
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = channel.pipeline();//底层是双向链表
        
        System.out.println("服务器线程 " + Thread.currentThread().getName());
        System.out.println("客户端地址 " + channel.remoteAddress());
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送 " + byteBuf.toString(CharsetUtil.UTF_8));


    }

    /**
     * 数据读取完成
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello client", CharsetUtil.UTF_8);
        // 写入通道，并刷新
        ctx.writeAndFlush(byteBuf);
    }

    /**
     * 异常处理，一般需要关闭通道
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
