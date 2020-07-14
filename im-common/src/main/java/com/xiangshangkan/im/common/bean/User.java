package com.xiangshangkan.im.common.bean;

import com.xiangshangkan.im.common.bean.msg.ProtoMsg;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: ${description}
 * @Author: Zohar
 * @Date: 2020/7/11 10:48
 * @Version: 1.0
 */
@Slf4j
@Data
public class User {

    String uid;
    String devId;
    String token;
    String nickName = "nickName";
    PLATTYPE platform = PLATTYPE.WINDOWS;

    // 平台
    public enum  PLATTYPE {
        WINDOWS, MAC, ANDROID, IOS, WEB, OTHRE;
    }

    private String sessionId;

    public void setPlatform(int platfrom) {
        PLATTYPE[] values = PLATTYPE.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].ordinal() == platfrom) {
                this.platform = values[i];
            }
        }
    }




    public static User fromMsg(ProtoMsg.LoginRequest info) {
        User user = new User();
        user.uid = new String(info.getUid());
        user.devId = new String(info.getDeviceId());
        user.token = new String(info.getToken());
        user.setPlatform(info.getPlatform());
        log.info("登录中：{}", user.toString());
        return user;
    }

}
