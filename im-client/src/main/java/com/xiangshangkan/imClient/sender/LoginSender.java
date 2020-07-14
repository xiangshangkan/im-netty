package com.xiangshangkan.imClient.sender;

import com.xiangshangkan.imClient.protoBuilder.LoginMsgBuilder;
import com.zhh.im.bean.msg.ProtoMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description: ${description}
 * @Author: Zohar
 * @Date: 2020/7/11 14:06
 * @Version: 1.0
 */

@Slf4j
@Service("loginSender")
public class LoginSender extends BaseSender{

    public void sendLoginMsg(){
       if (!isConnected()) {
           log.info("还没有建立连接");
           return;
       }
       log.info("构造登录消息");
        ProtoMsg.Message message =
                LoginMsgBuilder.buildLoginMsg(getUser(),getSession());
        log.info("发送登录消息");
        super.sendMsg(message);
    }
}
