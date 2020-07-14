package com.xiangshangkan.imClient.command;

import java.util.Scanner;

/**
 * @Description: ${description}
 * @Author: Zohar
 * @Date: 2020/7/11 10:25
 * @Version: 1.0
 */
public interface BaseCommand {

    void exec(Scanner scan);

    String getKey();

    String getTip();
}
