package com.xiangshangkan.imClient.protoBuilder;

import com.xiangshangkan.imClient.client.ClientSession;
import com.zhh.im.bean.msg.ProtoMsg;

/**
 * @Description: 基础Builder
 * @Author: Zohar
 * @Date: 2020/7/11 15:24
 * @Version: 1.0
 */
public class BaseBuilder {
    protected ProtoMsg.HeadType type;
    private long seqId;
    private ClientSession session;

    public BaseBuilder(ProtoMsg.HeadType type, ClientSession session) {
        this.type = type;
        this.session = session;
    }

    /**
     * 构建消息基础部分
     * @param seqId
     * @return
     */
    public ProtoMsg.Message buildCommon(long seqId) {
        this.seqId = seqId;

        ProtoMsg.Message.Builder mb =
                ProtoMsg.Message
                .newBuilder()
                .setType(type)
                .setSessionId(session.getSessionId())
                .setSequence(seqId);
        return mb.buildPartial();
    }
}
