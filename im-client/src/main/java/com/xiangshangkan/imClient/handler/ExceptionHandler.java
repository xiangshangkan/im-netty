package com.xiangshangkan.imClient.handler;

import com.xiangshangkan.im.common.exception.InvalidFrameException;
import com.xiangshangkan.imClient.client.ClientSession;
import com.xiangshangkan.imClient.client.CommandController;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: ${description}
 * @Author: Zohar
 * @Date: 2020/7/13 14:11
 * @Version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
@Service("exceptionHandler")
public class ExceptionHandler extends ChannelInboundHandlerAdapter{

    @Autowired
    private CommandController commandController;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        if (cause instanceof InvalidFrameException) {
            log.error(cause.getMessage());
            ClientSession.getSession(ctx).close();
        } else {

            //捕捉异常信息
            log.error(cause.getMessage());
            ctx.close();

            //开始重连
            commandController.setConnectFlag(false);
            commandController.startConnectServer();
        }
    }

    /**
     * 通道 Read 读取 Complete 完成
     * 做刷新操作 ctx.flush()
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
