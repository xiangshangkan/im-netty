package com.xiangshangkan.imClient.protoBuilder;

import com.xiangshangkan.im.common.bean.User;
import com.xiangshangkan.imClient.client.ClientSession;
import com.zhh.im.bean.msg.ProtoMsg;

/**
 * @Description: 心跳消息Builder
 * @Author: Zohar
 * @Date: 2020/7/13 14:41
 * @Version: 1.0
 */
public class HeartBeatMsgBuilder extends BaseBuilder{
    private final User user;


    public HeartBeatMsgBuilder(User user, ClientSession session) {
        super(ProtoMsg.HeadType.HEART_BEAT, session);
        this.user = user;
    }

    public ProtoMsg.Message buildMsg(){
        ProtoMsg.Message message = buildCommon(-1);
        ProtoMsg.MessageHeartBeat.Builder lb =
                ProtoMsg.MessageHeartBeat.newBuilder()
                .setSeq(0)
                .setJson("{\"from\":\"client\"}")
                .setUid(user.getUid());
        return message.toBuilder().setHeartBeat(lb).build();
    }
}
