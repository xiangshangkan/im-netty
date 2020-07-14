package com.xiangshangkan.imClient;

import com.xiangshangkan.imClient.client.CommandController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ImClientApplication {

	public static void main(String[] args) {
		//启动并初始化 Spring 环境及其各 Spring组件
		ApplicationContext context =
				SpringApplication.run(ImClientApplication.class, args);
		CommandController commandClient =
				context.getBean(CommandController.class);

		commandClient.initCommandMap();

		commandClient.startCommandThread();
	}

}
