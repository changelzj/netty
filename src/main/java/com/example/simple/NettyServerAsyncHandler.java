package com.example.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.util.CharsetUtil;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class NettyServerAsyncHandler extends ChannelInboundHandlerAdapter {
    /**
     * 用户自定义普通任务
     * 在channel对应的taskQueue中异步执行
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        channel.eventLoop().execute(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                TimeUnit.SECONDS.sleep(10);
                ByteBuf byteBuf = (ByteBuf) msg;
                System.out.println("客户端发送 " + byteBuf.toString(CharsetUtil.UTF_8));
            }
        });
        
        System.out.println("channelRead");

    }

    /**
     * 数据读取完成
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        EventLoop eventExecutors = ctx.channel().eventLoop();
        eventExecutors.schedule(() -> {
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello client", CharsetUtil.UTF_8);
            ctx.writeAndFlush(byteBuf);
        }, 5, TimeUnit.SECONDS);
        
        System.out.println("channelReadComplete");
    }

    /**
     * 异常处理，一般需要关闭通道
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
