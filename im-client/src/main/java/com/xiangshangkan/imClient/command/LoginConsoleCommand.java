package com.xiangshangkan.imClient.command;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * @Description: 登录命令的信息收集类，负责从Scanner 控制台实例收集客户端登录的用户ID和密码
 * @Author: Zohar
 * @Date: 2020/7/10 10:13
 * @Version: 1.0
 */
@Data
@Service("loginConsoleCommand")
public class LoginConsoleCommand implements BaseCommand{

    public static final String KEY = "1";

    private String userName;
    private String password;

    @Override
    public void exec(Scanner scan) {
        System.out.println("请输入用户信息（id : password）");
        String[] info = null;
        while(true) {
            String input = scan.next();
            info = input.split(";");
            if (info.length != 2) {
                System.out.println("请按照格式输入（id : password）:");
            } else {
                break;
            }
        }

        userName = info[0];
        password = info[1];
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "登录";
    }
}
