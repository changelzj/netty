package com.example.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws Exception {
        // 线程数是默认逻辑CPU数的两倍
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        ServerBootstrap bootstrap = new ServerBootstrap();


        NettyServerHandler handler = new NettyServerHandler();
        NettyServerAsyncHandler asyncHandler = new NettyServerAsyncHandler();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(asyncHandler);
                    }
                });
        
        System.out.println("server init");
        // 绑定端口并启动
        ChannelFuture future = bootstrap.bind(8888).sync();
        
        // 注册监听器，监控关心的事件
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("success");
                } else if (future.isDone()) {
                    
                }
            }
        });
        
        
        
        
        // 监听关闭通道
        future.channel().closeFuture().sync();
    }
}
