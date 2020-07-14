package com.xiangshangkan.imClient.protoBuilder;

import com.xiangshangkan.im.common.bean.ChatMsg;
import com.xiangshangkan.im.common.bean.User;
import com.xiangshangkan.imClient.client.ClientSession;
import com.zhh.im.bean.msg.ProtoMsg;

/**
 * @Description:  聊天消息Builder
 * @Author: Zohar
 * @Date: 2020/7/11 18:16
 * @Version: 1.0
 */
public class ChatMsgBuilder extends BaseBuilder{

    private ChatMsg chatMsg;
    private User user;

    public ChatMsgBuilder(ChatMsg chatMsg, User user, ClientSession session) {
        super(ProtoMsg.HeadType.MESSAGE_REQUEST,session);
        this.chatMsg = chatMsg;
        this.user = user;
    }

    public ProtoMsg.Message build(){
        ProtoMsg.Message message = buildCommon(-1);
        ProtoMsg.MessageRequest.Builder cb
                = ProtoMsg.MessageRequest.newBuilder();

        chatMsg.fillMsg(cb);
        return message
                .toBuilder()
                .setMessageRequest(cb)
                .build();
    }

    public static ProtoMsg.Message buildChatMsg(
            ChatMsg chatMsg,
            User user,
            ClientSession session) {
        ChatMsgBuilder builder =
                new ChatMsgBuilder(chatMsg,user,session);
        return builder.build();
    }
}
