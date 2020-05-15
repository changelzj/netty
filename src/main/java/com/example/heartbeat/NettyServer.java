package com.example.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class NettyServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup parent = new NioEventLoopGroup();
        EventLoopGroup child = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(parent, child)
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        // 处理空闲状态的处理器 
                        // readerIdleTime 多长时间没有读，发送心跳检测是否连接
                        // writerIdleTime 多长时间没有写，发送心跳检测是否连接
                        // allIdleTime 多长时间没有读写，发送心跳检测是否连接
                        pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                        // 加入空闲检测进一步处理的handler
                        pipeline.addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                Channel channel = ctx.channel();
                                if (evt instanceof IdleStateEvent) {
                                    IdleStateEvent stateEvent = (IdleStateEvent) evt;
                                    switch (stateEvent.state()) {
                                        case READER_IDLE:
                                            System.out.println(channel + "READER_IDLE");
                                            break;
                                        case ALL_IDLE:
                                            System.out.println(channel + "ALL_IDLE");
                                            break;
                                        case WRITER_IDLE:
                                            System.out.println(channel + "WRITER_IDLE");
                                            break;
                                    }
                                }
                            }
                        });
                    }
                });
        ChannelFuture future = bootstrap.bind(8990).sync();
        future.channel().closeFuture().sync();
    }
}
