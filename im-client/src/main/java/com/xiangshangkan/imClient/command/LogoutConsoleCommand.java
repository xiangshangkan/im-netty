package com.xiangshangkan.imClient.command;

import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * @Description: ${description}
 * @Author: Zohar
 * @Date: 2020/7/13 11:39
 * @Version: 1.0
 */
@Service("logoutConsoleCommand")
public class LogoutConsoleCommand implements BaseCommand{
    public static final String KEY = "10";

    @Override
    public void exec(Scanner scan) {

    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "退出";
    }
}
