package com.xiangshangkan.imClient.client;

import com.xiangshangkan.im.common.bean.User;
import com.xiangshangkan.im.common.codec.ProtobufDecoder;
import com.xiangshangkan.im.common.codec.ProtobufEncoder;
import com.xiangshangkan.imClient.handler.ChatMsgHandler;
import com.xiangshangkan.imClient.handler.ExceptionHandler;
import com.xiangshangkan.imClient.handler.LoginResponceHandler;
import com.xiangshangkan.imClient.sender.ChatSender;
import com.xiangshangkan.imClient.sender.LoginSender;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Description: ${description}
 * @Author: Zohar
 * @Date: 2020/7/11 16:34
 * @Version: 1.0
 */
@Slf4j
@Data
@Service("nettyClient")
public class NettyClient {

    //服务器ip地址
    @Value("${server.ip}")
    private String host;
    //服务器端口
    @Value("${server.port}")
    private int port;

    @Autowired
    private ChatMsgHandler chatMsgHandler;

    @Autowired
    private LoginResponceHandler loginResponceHandler;

    @Autowired
    private ExceptionHandler exceptionHandler;

    private Channel channel;
    private ChatSender chatSender;
    private LoginSender loginSender;

    /**
     * 唯一标记
     */
    private boolean initFlag = true;
    private User user;
    private GenericFutureListener<ChannelFuture> connectedListener;

    private Bootstrap bootstrap;
    private EventLoopGroup group;

    public NettyClient(){

        /**
         * 客户端的是Bootstrap，服务端的则是 ServerBootstrap
         * 都是AbstractBootstrap 的子类
         */

        /**
         * 通过 nio 方式来接收连接和处理连接
         */
        group = new NioEventLoopGroup();
    }

    /**
     * 重连
     */
    public void doConnect(){
        try {
            bootstrap = new Bootstrap();

            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.remoteAddress(host,port);

            //设置通道初始化
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("decoder",new ProtobufDecoder());
                    ch.pipeline().addLast("encoder",new ProtobufEncoder());
                    ch.pipeline().addLast(loginResponceHandler);
                    ch.pipeline().addLast(chatMsgHandler);
                    ch.pipeline().addLast(exceptionHandler);
                }
            });
            log.info("客户端开始连接");

            ChannelFuture future = bootstrap.connect();
            future.addListener(connectedListener);
        } catch (Exception e) {
            log.info("客户端连接失败！" + e.getMessage());
        }
    }

    public void close(){
        group.shutdownGracefully();
    }
}
