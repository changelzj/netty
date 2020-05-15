package com.example.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

public class NettyServer {
    
    public static void main(String[] args) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                        //.addLast(new ProtobufDecoder(StudentDTO.Student.getDefaultInstance()))
                        .addLast(new ProtobufDecoder(DTO.Obj.getDefaultInstance()))
                        .addLast(new SimpleChannelInboundHandler<DTO.Obj>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, DTO.Obj msg) throws Exception {
                                if (msg.getType() == DTO.Obj.Dtype.studentType) {
                                    System.out.println(msg);
                                }
                            }
                        });
                    }
                });


        ChannelFuture future = bootstrap.bind(82).sync();
        
        future.channel().closeFuture().sync();
    }
}
