package com.example.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class NettyClient {

    @ChannelHandler.Sharable
    static class Handler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            //StudentDTO.Student meiluo = StudentDTO.Student.newBuilder().setId(1).setName("meiluo").build();
            DTO.Student zhaoly = DTO.Student.newBuilder().setId(1).setName("赵丽颖").build();
            DTO.Obj obj = DTO.Obj.newBuilder().setType(DTO.Obj.Dtype.studentType).setStudent(zhaoly).build();
            ctx.writeAndFlush(obj);
        }
    }
    
    public static void main(String[] args) throws Exception {

        EventLoopGroup loopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(loopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ProtobufEncoder())
                                .addLast(new Handler());
                    }
                });

        ChannelFuture future = bootstrap.connect("127.0.0.1", 82);
        future.channel().closeFuture().sync();
    }
}
