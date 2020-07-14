package com.xiangshangkan.imClient.handler;

import com.xiangshangkan.im.common.bean.User;
import com.xiangshangkan.imClient.client.ClientSession;
import com.xiangshangkan.imClient.protoBuilder.HeartBeatMsgBuilder;
import com.zhh.im.bean.msg.ProtoMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Description: ${description}
 * @Author: Zohar
 * @Date: 2020/7/13 14:36
 * @Version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
@Service("heartBeatClientHandler")
public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter{

    //心跳的时间间隔
    private static final int HEARTBEAT_INTERVAL = 100;

    /**
     * 在Handler 被加入到 Pipeline 时，开始发送心跳
     * @param ctx
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        ClientSession session = ClientSession.getSession(ctx);
        User user = session.getUser();
        HeartBeatMsgBuilder builder =
                new HeartBeatMsgBuilder(user,session);

        ProtoMsg.Message message = builder.buildMsg();
        //发送心跳
        heartBeat(ctx,message);
    }

    /**
     * 使用定时器，发送心跳报文
     * @param ctx
     * @param heartbeatMsg
     */
    public void heartBeat(ChannelHandlerContext ctx, ProtoMsg.Message heartbeatMsg) {
        ctx.executor().schedule(()->{

            if (ctx.channel().isActive()) {
                log.info("发送 HEART_BEAT 消息 to server");
                ctx.writeAndFlush(heartbeatMsg);

                //递归调用
                heartBeat(ctx,heartbeatMsg);
            }
        },HEARTBEAT_INTERVAL ,TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //判断消息实例
        if (null == msg ||!(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx,msg);
            return;
        }

        //判断类型
        ProtoMsg.Message pkg = (ProtoMsg.Message)msg;
        ProtoMsg.HeadType headType = pkg.getType();
        if (headType.equals(ProtoMsg.HeadType.HEART_BEAT)) {

            log.info(" 收到回写的 HEART_BEAT 消息 from server");

            return;
        } else {
            super.channelRead(ctx,msg);
        }
    }

}
