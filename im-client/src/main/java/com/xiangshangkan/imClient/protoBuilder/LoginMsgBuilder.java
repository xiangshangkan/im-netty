package com.xiangshangkan.imClient.protoBuilder;

import com.xiangshangkan.im.common.bean.User;
import com.xiangshangkan.imClient.client.ClientSession;
import com.zhh.im.bean.msg.ProtoMsg;


/**
 * @Description: 登录消息 Builder
 * @Author: Zohar
 * @Date: 2020/7/11 15:23
 * @Version: 1.0
 */
public class LoginMsgBuilder extends BaseBuilder{
    private final User user;

    public LoginMsgBuilder(User user, ClientSession session) {
        super(ProtoMsg.HeadType.LOGIN_REQUEST, session);
        this.user = user;
    }

    public ProtoMsg.Message build(){
        ProtoMsg.Message message = buildCommon(-1);
        ProtoMsg.LoginRequest.Builder lb =
                ProtoMsg.LoginRequest.newBuilder()
                .setDeviceId(user.getDevId())
                .setPlatform(user.getPlatform().ordinal())
                .setToken(user.getToken())
                .setUid(user.getUid());

        return message.toBuilder().setLoginRequest(lb).build();
    }

    public static ProtoMsg.Message buildLoginMsg(User user, ClientSession session) {
        LoginMsgBuilder builder = new LoginMsgBuilder(user,session);
        return builder.build();
    }
}
