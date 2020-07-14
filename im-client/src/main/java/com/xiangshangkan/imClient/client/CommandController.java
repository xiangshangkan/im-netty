package com.xiangshangkan.imClient.client;

import com.xiangshangkan.im.common.bean.User;
import com.xiangshangkan.im.common.concurrent.FutureTaskScheduler;
import com.xiangshangkan.imClient.command.*;
import com.xiangshangkan.imClient.sender.ChatSender;
import com.xiangshangkan.imClient.sender.LoginSender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @Description: 成功获取到用户密码和 ID 后，客户端 CommandClient 将这些内容组装成User POJO 用户对象，然后
 *                 通过客户端的发送器 loginSender 开始向服务器端发送登录请求
 * @Author: Zohar
 * @Date: 2020/7/11 10:34
 * @Version: 1.0
 */
@Slf4j
@Data
@Service("/commandController")
public class CommandController {

    //聊天命令收集类
    @Autowired
    ChatConsoleCommand chatConsoleCommand;

    //登录命令收集类
    @Autowired
    LoginConsoleCommand loginConsoleCommand;

    //登出命令收集类
    @Autowired
    LogoutConsoleCommand logoutConsoleCommand;

    //菜单命令收集类
    @Autowired
    ClientCommandMenu clientCommandMenu;

    private Map<String,BaseCommand> commandMap;

    private String menuString;

    //会话类
    private ClientSession session;

    @Autowired
    private NettyClient nettyClient;

    private Channel channel;

    @Autowired
    private ChatSender chatSender;

    @Autowired
    private LoginSender loginSender;

    private boolean connectFlag = false;
    private User user;

    GenericFutureListener<ChannelFuture> closeListener = (ChannelFuture f) ->
    {
        log.info(new Date() + "连接已经断开");
        channel = f.channel();

        //创建会话
        ClientSession session =
                channel.attr(ClientSession.SESSION_KEY).get();
        session.close();

        //唤醒用户线程
        notifyCommandThread();
    };

    GenericFutureListener<ChannelFuture> connectedListener = (ChannelFuture f) -> {
        final EventLoop eventLoop =
                f.channel().eventLoop();

        if (!f.isSuccess()) {
            log.info("连接失败！在10s 之后准备尝试重连！");
            eventLoop.schedule(
                    () -> nettyClient.doConnect(),
                    10,
                    TimeUnit.SECONDS
            );
            connectFlag =false;
        } else {
            connectFlag = true;

            log.info("IM 服务器 连接成功！");
            channel = f.channel();

            //创建会话
            session = new ClientSession(channel);
            session.setConnected(true);
            channel.closeFuture().addListener(closeListener);

            //唤醒用户线程
            notifyCommandThread();
        }
    };

    public void initCommandMap(){
        commandMap = new HashMap<>();
        commandMap.put(clientCommandMenu.getKey(),clientCommandMenu);
        commandMap.put(chatConsoleCommand.getKey(),chatConsoleCommand);
        commandMap.put(loginConsoleCommand.getKey(),loginConsoleCommand);
        commandMap.put(logoutConsoleCommand.getKey(),logoutConsoleCommand);

        Set<Map.Entry<String,BaseCommand>> entries =
                commandMap.entrySet();
        Iterator<Map.Entry<String,BaseCommand>> iterator =
                entries.iterator();

        StringBuilder menus = new StringBuilder();
        menus.append("[menu]");
        while(iterator.hasNext()) {
            BaseCommand next = iterator.next().getValue();

            menus.append(next.getKey())
                    .append("->")
                    .append(next.getTip())
                    .append(" | ");
        }

        menuString = menus.toString();

        clientCommandMenu.setAllCommandsShow(menuString);
    }

    public void startConnectServer(){

        FutureTaskScheduler.add(()->{
            nettyClient.setConnectedListener(connectedListener);
            nettyClient.doConnect();
        });
    }

    public synchronized void notifyCommandThread(){
        //唤醒，命令收集程
        this.notify();
    }

    public synchronized void waitCommandThread(){

        //休眠，命令收集线程
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startCommandThread(){
        Thread.currentThread().setName("主线程");
        while (true) {
            //建立连接
            while(connectFlag == false) {
                //开始连接
                startConnectServer();
                waitCommandThread();
            }
            //处理命令
            while (null != session) {

                Scanner scanner = new Scanner(System.in);
                clientCommandMenu.exec(scanner);
                String key = clientCommandMenu.getCommandInput();
                BaseCommand command = commandMap.get(key);

               if (null == command) {
                   System.err.println("无法识别【" + command + "】指令，请重新输入！");
                   continue;
               }

               switch (key) {
                   case ChatConsoleCommand.KEY:
                       command.exec(scanner);
                       startOneChat((ChatConsoleCommand) command);
                       break;

                   case LoginConsoleCommand.KEY:
                       command.exec(scanner);
                       startLogin((LoginConsoleCommand) command);
                       break;

                   case LogoutConsoleCommand.KEY:
                       command.exec(scanner);
                       startLogout(command);
                       break;
               }
            }
        }

    }

    /**
     * 发送单聊消息
     * @param c
     */
    private void startOneChat(ChatConsoleCommand c) {
        //登录
        if (!isLogin()) {
            log.info("还没有登录，请先登录");
            return;
        }
        chatSender.setSession(session);
        chatSender.setUser(user);
        chatSender.sendChatMsg(c.getToUserId(),c.getMessage());

    }

    /**
     * 开始发送登录请求
     * @param command
     */
    private void startLogin(LoginConsoleCommand command) {
        //登录
        if (!connectFlag) {
            log.info("连接异常，请重新建立连接");
            return;
        }
        User user = new User();
        user.setUid(command.getUserName());
        user.setToken(command.getPassword());
        user.setDevId("1111");
        this.user = user;
        session.setUser(user);
        loginSender.setUser(user);
        loginSender.setSession(session);
        loginSender.sendLoginMsg();

    }

    private void startLogout(BaseCommand command) {
        //登出
        if (!isLogin()) {
            log.info("还没有登录，请先登录！");
            return;
        }

        //todo 登出
    }

    public boolean isLogin(){
        if (null == session) {
            log.info("session is null");
            return false;
        }

        return session.isLogin();
    }

}
