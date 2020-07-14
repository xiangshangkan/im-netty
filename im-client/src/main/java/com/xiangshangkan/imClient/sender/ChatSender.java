package com.xiangshangkan.imClient.sender;

import com.xiangshangkan.im.common.bean.ChatMsg;
import com.xiangshangkan.imClient.protoBuilder.ChatMsgBuilder;
import com.zhh.im.bean.msg.ProtoMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description: ${description}
 * @Author: Zohar
 * @Date: 2020/7/13 13:41
 * @Version: 1.0
 */
@Slf4j
@Service("chatSender")
public class ChatSender extends BaseSender{

    public void sendChatMsg(String touid, String content) {
        log.info("发送消息 startConnectServer ");
        ChatMsg chatMsg = new ChatMsg(getUser());
        chatMsg.setContent(content);
        chatMsg.setMsgtype(ChatMsg.MSGTYPE.TEXT);
        chatMsg.setTo(touid);
        chatMsg.setMsgId(System.currentTimeMillis());
        ProtoMsg.Message message =
                ChatMsgBuilder.buildChatMsg(chatMsg,getUser(),getSession());

        super.sendMsg(message);
    }

    @Override
    protected void sendSucced(ProtoMsg.Message message) {
        log.info("发送成功：" + message.getMessageRequest().getContent());
    }

    protected void sendFailed(ProtoMsg.Message message){
        log.info("发送失败：" +  message.getMessageRequest().getContent());
    }
}
