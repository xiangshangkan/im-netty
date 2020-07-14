package com.xiangshangkan.imClient.command;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * @Description: ${description}
 * @Author: Zohar
 * @Date: 2020/7/13 11:57
 * @Version: 1.0
 */
@Data
@Service("chatConsoleCommand")
public class ChatConsoleCommand implements BaseCommand{

    private String toUserId;
    private String message;
    public static final String KEY = "2";
    @Override
    public void exec(Scanner scan) {
        System.out.println("请输入聊天的消息（id : message）：");
        String[] info = null;
        while(true) {
            String input = scan.next();
            info = input.split(":");
            if (info.length != 2) {
                System.out.println("请输入聊天的消息（id : message）：");
            } else {
                break;
            }
        }
        toUserId = info[0];
        message = info[1];
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "聊天";
    }
}
