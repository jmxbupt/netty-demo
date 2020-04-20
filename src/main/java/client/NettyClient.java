package client;

import client.console.ConsoleCommandManager;
import client.handler.*;
import codec.PacketDecoder;
import codec.PacketEncoder;
import codec.Spliter;
import handler.IMIdleStateHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author jmx
 * @date 2020/3/9 10:44 PM
 */
public class NettyClient {

    private static final int MAX_RETRY = 5;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8000;

    private static Bootstrap bootstrap;

    private static ConsoleThread consoleThread;

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap = new Bootstrap();

        bootstrap
                .group(group)
                .channel(NioSocketChannel.class)
                // 绑定自定义属性到 channel
                .attr(AttributeKey.newInstance("clientName"), "nettyClient")
                // 设置TCP底层属性
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        // 空闲检测
                        ch.pipeline().addLast(new IMIdleStateHandler());
                        // 重连处理器
                        ch.pipeline().addLast(new ReConnectHandler());
                        // 拆包器
                        ch.pipeline().addLast(new Spliter());
                        // 解码器
                        ch.pipeline().addLast(new PacketDecoder());

                        // 注册响应处理器
                        ch.pipeline().addLast(new RegisterResponseHandler());
                        // 登录响应处理器
                        ch.pipeline().addLast(new LoginResponseHandler());

                        // 获取好友列表响应处理器
                        ch.pipeline().addLast(new ListContactsResponseHandler());
                        // 加好友请求响应处理器
                        ch.pipeline().addLast(new ContackAskResponseHandler());
                        // 加好友确认响应处理器
                        ch.pipeline().addLast(new ContactConfirmResponseHandler());
                        // 删除好友响应处理器
                        ch.pipeline().addLast(new ContactDeleteResponseHandler());

                        // 单聊响应处理器
                        ch.pipeline().addLast(new MessageResponseHandler());

                        // 创建群响应处理器
                        ch.pipeline().addLast(new CreateGroupResponseHandler());
                        // 获取群成员响应处理器
                        ch.pipeline().addLast(new ListGroupMembersResponseHandler());
                        // 加入群响应处理器
                        ch.pipeline().addLast(new JoinGroupResponseHandler());
                        // 退出群响应处理器
                        ch.pipeline().addLast(new QuitGroupResponseHandler());
                        // 群消息响应处理器
                        ch.pipeline().addLast(new GroupMessageResponseHandler());

                        // 登出响应处理器
                        ch.pipeline().addLast(new LogoutResponseHandler());

                        // 编码器
                        ch.pipeline().addLast(new PacketEncoder());
                        // 心跳定时器
                        ch.pipeline().addLast(new HeartbeatTimerHandler());
                    }
                });

        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    // 用于logout或者服务端主动断开之后，客户端自动重连
    public static void reConnect() {
        // 这里使用标志位而不用consoleThread.interrupt()方法的原因是
        // consoleThread内部会有sleep操作，无法中断
        consoleThread.running = false;
        try {
            consoleThread.join();
        } catch (InterruptedException e) {
            // 忽略
        }
        System.out.println("重新连接服务器...");
        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    private static void connect(final Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {

                Channel channel = ((ChannelFuture) future).channel();

                /*
                 * 为什么要在这里新创建一个线程来进行接收控制台输入的数据和发送给服务端呢？
                 * 解答：
                 *      因为调用本办法是在监听中，而监听是用来监视客户端是否与服务端连接上了的
                 *      监听会一直不间断的获取结果，一旦连接成功了，就会调用本方法来打开控制台接收数据
                 *      所以如果我们在本方法中直接使用while循环的话，就会造成主线程堵塞在这里，无法处理回调
                 */


                // 不再重新启动线程，而是直接用一个hasLogout标识位 + while循环【不行！！！】
//                System.out.println(new Date() + ": 连接成功，启动控制台...");
//                while (!ConsoleCommandManager.hasLogout) {
//                    ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
//                    Scanner scanner = new Scanner(System.in);
//                    consoleCommandManager.exec(scanner, channel);
//                }


                // 启动控制台线程
                System.out.println(new Date() + ": 连接成功，启动控制台线程...");
//                startConsoleThread(channel);
                consoleThread = new ConsoleThread(channel);
                consoleThread.start();

            } else if (retry == 0) {
                System.out.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = MAX_RETRY - retry + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.out.println(new Date() + "：连接失败，第" + order + "次重连...");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay,
                        TimeUnit.SECONDS);
            }
        });
    }

    private static void startConsoleThread(Channel channel) {

        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
        Scanner scanner = new Scanner(System.in);

        new Thread(() -> {
            while (!Thread.interrupted()) {
                consoleCommandManager.exec(scanner, channel);
            }
        }).start();
    }
}

class ConsoleThread extends Thread {

    private Channel channel;
    private ConsoleCommandManager consoleCommandManager;
    private Scanner scanner;

    public volatile boolean running = true;

    public ConsoleThread(Channel channel) {
        this.channel = channel;
        consoleCommandManager = new ConsoleCommandManager();
        scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        while (running) {
            consoleCommandManager.exec(scanner, channel);
        }
    }
}
