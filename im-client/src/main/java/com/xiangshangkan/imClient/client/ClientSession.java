package com.xiangshangkan.imClient.client;

import com.xiangshangkan.im.common.bean.User;
import com.zhh.im.bean.msg.ProtoMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 实现客户端 Session 会话
 * @Author: Zohar
 * @Date: 2020/7/11 14:31
 * @Version: 1.0
 */
@Slf4j
@Data
public class ClientSession {

    public static final AttributeKey<ClientSession> SESSION_KEY = AttributeKey.valueOf("SESSION_KEY");

    /**
     * 用户实现客户端会话管理的核心
     */
    private Channel channel;
    private User user;

    /**
     * 保存登录后的服务端sessionId
     */
    private String sessionId;

    private boolean isConnected = false;
    private boolean isLogin = false;
    /**
     * session 中存储的 session 变量属性值
     */
    private Map<String,Object> map = new HashMap<>();

    /**
     * 绑定通道
     * @param channel
     */
    public ClientSession(Channel channel) {
        this.channel = channel;
        this.sessionId = String.valueOf(-1);
        channel.attr(ClientSession.SESSION_KEY).set(this);
    }

    /**
     * 登录成功之后，设置sessionId
     * @param ctx
     * @param pkg
     */
    public static void loginSuccess(ChannelHandlerContext ctx, ProtoMsg.Message pkg) {
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        session.setSessionId(pkg.getSessionId());
        session.setLogin(true);
        log.info("登录成功！");
    }

    /**
     * 获取channel
     * @param ctx
     * @return
     */
    public static ClientSession getSession(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        return session;
    }

    /**
     * 获取ip
     * @return
     */
    public String getRemoteAddress(){
        return channel.remoteAddress().toString();
    }

    /**
     * 写 protoBuf 数据帧
     * @param pkg
     * @return
     */
    public ChannelFuture writeAndFlush(Object pkg) {
        ChannelFuture f = channel.writeAndFlush(pkg);
        return f;
    }

    public void writeAndClose(Object pkg) {
        ChannelFuture f = channel.writeAndFlush(pkg);
        f.addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 关闭通道
     */
    public void close(){
        isConnected = false;

        ChannelFuture future = channel.close();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.error("连接顺利断开");
                }
            }
        });
    }
}
