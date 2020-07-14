package com.xiangshangkan.imClient.sender;

import com.xiangshangkan.im.common.bean.User;
import com.xiangshangkan.imClient.client.ClientSession;
import com.zhh.im.bean.msg.ProtoMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * @Description: ${description}
 * @Author: Zohar
 * @Date: 2020/7/11 14:06
 * @Version: 1.0
 */
@Data
@Slf4j
public abstract class BaseSender {

    private User user;
    private ClientSession session;

    /**
     * 是否已连接
     * @return
     */
    public boolean isConnected(){
        if (null == session) {
            log.info("session is null");
            return false;
        }
        return session.isConnected();
    }

    /**
     * 是否已登录
     * @return
     */
    public boolean isLogin(){
        if (null == session) {
            log.info("session is null");
        }
        return session.isLogin();
    }

    /**
     * 发送消息
     * @param message
     */
    public void sendMsg(ProtoMsg.Message message) {

        if (null == getSession() || !isConnected()) {
            log.info("连接还没成功");
        }

        Channel channel = getSession().getChannel();
        ChannelFuture f = channel.writeAndFlush(message);
        f.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                //回调
                if (future.isSuccess()) {
                    sendSucced(message);
                } else {
                    sendFail(message);
                }
            }
        });
    }

    protected void sendSucced(ProtoMsg.Message message) {
        log.info("发送成功");
    }

    protected void sendFail(ProtoMsg.Message message) {
        log.info("发送失败");
    }

}
