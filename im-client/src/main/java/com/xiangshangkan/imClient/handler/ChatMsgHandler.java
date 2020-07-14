package com.xiangshangkan.imClient.handler;

import com.xiangshangkan.im.common.bean.msg.ProtoMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Service;

/**
 * @Description: ${description}
 * @Author: Zohar
 * @Date: 2020/7/13 10:20
 * @Version: 1.0
 */
@ChannelHandler.Sharable
@Service
public class ChatMsgHandler extends ChannelInboundHandlerAdapter {

    public ChatMsgHandler(){

    }

    /**
     * 业务逻辑处理
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //判断消息实例
        if (null == msg || !(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        //判断类型
        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType headType = pkg.getType();
        if (!headType.equals(ProtoMsg.HeadType.MESSAGE_REQUEST)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMsg.MessageRequest req = pkg.getMessageRequest();
        String content = req.getContent();
        String uid = req.getFrom();

        System.out.println("收到消息 from uid:" + uid + "->" + content);
    }
}
