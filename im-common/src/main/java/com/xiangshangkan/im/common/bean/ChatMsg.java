package com.xiangshangkan.im.common.bean;

import com.zhh.im.bean.msg.ProtoMsg;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @Description: ${description}
 * @Author: Zohar
 * @Date: 2020/7/11 18:18
 * @Version: 1.0
 */
@Data
public class ChatMsg {

    //消息类型 1：纯文本  2 ：音频  3：视频  4：地理位置 5：其他
    public enum MSGTYPE {
        TEXT,
        AUDIO,
        VIDEO,
        POS,
        OTHRE
    }

    private User user;

    private long msgId;
    private String from;
    private String to;
    private long time;
    private MSGTYPE msgtype;
    private String content;
    private String url;    //多媒体地址
    private String property; // 附加属性
    private String fromNick; //发送者昵称
    private String json; //附加的json串

    public ChatMsg(User user) {
        if (null == user) {
            return;
        }
        this.user = user;
        this.setTime(System.currentTimeMillis());
        this.setFrom(user.getUid());
        this.setFromNick(user.getNickName());
    }

    public void fillMsg(ProtoMsg.MessageRequest.Builder cb) {
        if (msgId > 0) {
            cb.setMsgId(msgId);
        }
        if (!StringUtils.isEmpty(from)) {
            cb.setFrom(from);
        }
        if (!StringUtils.isEmpty(to)) {
            cb.setTo(to);
        }
        if (time > 0) {
            cb.setTime(time);
        }
        if (msgtype != null) {
            cb.setMsgType(msgtype.ordinal());
        }
        if (!StringUtils.isEmpty(content)) {
            cb.setContent(content);
        }
        if (!StringUtils.isEmpty(url)) {
            cb.setUrl(url);
        }
        if (!StringUtils.isEmpty(property)) {
            cb.setProperty(property);
        }
        if (!StringUtils.isEmpty(fromNick)) {
            cb.setFromNick(fromNick);
        }

        if (!StringUtils.isEmpty(json)) {
            cb.setJson(json);
        }
    }
}
