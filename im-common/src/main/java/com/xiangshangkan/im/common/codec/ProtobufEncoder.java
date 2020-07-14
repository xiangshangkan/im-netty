package com.xiangshangkan.im.common.codec;

import com.xiangshangkan.im.common.ProtoInstant;
import com.xiangshangkan.im.common.bean.msg.ProtoMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Description: 编码器
 * @Author: Zohar
 * @Date: 2020/7/13 10:42
 * @Version: 1.0
 */
public class ProtobufEncoder extends MessageToByteEncoder<ProtoMsg.Message> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProtoMsg.Message message, ByteBuf byteBuf) throws Exception {
        byteBuf.writeShort(ProtoInstant.MAGIC_CODE);
        byteBuf.writeShort(ProtoInstant.VERSION_CODE);

        // 将对象转换为byte
        byte[] bytes = message.toByteArray();

        // 读取消息的长度
        int length = bytes.length;
        // 先将消息长度写入，也就是消息头
        byteBuf.writeInt(length);
        // 消息体中包含我们要发送的数据
        byteBuf.writeBytes(message.toByteArray());
    }
}
