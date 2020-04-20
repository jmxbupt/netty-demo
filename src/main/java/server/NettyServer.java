package server;

import codec.PacketCodecHandler;
import codec.Spliter;
import handler.IMIdleStateHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import server.handler.*;
import util.JDBCUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jmx
 * @date 2020/3/9 8:43 PM
 */

public class NettyServer {

    private static final int BEGIN_PORT = 8000;

    // 单机连接数
    public static AtomicInteger tcpCount = new AtomicInteger(0);
    // 在线用户数
    public static AtomicInteger userCount = new AtomicInteger(0);

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 启动一个Netty服务端，必须先定义一个引导类，然后为其指定三类属性，分别是线程模型、IO模型、连接读写处理逻辑，最后绑定端口
        // 还可以指定一些其他的属性（带child的是针对每条连接的）
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        final AttributeKey<Object> clientKey = AttributeKey.newInstance("clientKey");

        serverBootstrap
                // 指定线程模型
                .group(bossGroup, workerGroup)
                // 指定IO模型
                .channel(NioServerSocketChannel.class)
                .attr(AttributeKey.newInstance("serverName"), "nettyServer")
                .childAttr(clientKey, "clientValue")
                // 系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 开启TCP底层心跳机制
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // true表示关闭Nagle算法（用于减少数据发送次数），满足实时性要求
                .childOption(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<NioServerSocketChannel>() {
                    protected void initChannel(NioServerSocketChannel ch) {
                        System.out.println("服务器启动中...");
                        // 将uses表中所有用户的online置为FALSE（针对服务器之前崩溃的情况）
                        try (Connection conn = DriverManager.getConnection(JDBCUtil.JDBC_URL, JDBCUtil.JDBC_USER, JDBCUtil.JDBC_PASSWORD)) {
                            try (PreparedStatement ps = conn.prepareStatement(
                                    "UPDATE users SET online = FALSE")) {
                                ps.executeUpdate();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        System.out.println("用户登录状态重置完毕...");

                    }
                })
                // 指定连接读写处理逻辑
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new IMIdleStateHandler());
                        ch.pipeline().addLast(CountUserHandler.INSTANCE);
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(PacketCodecHandler.INSTANCE);
                        ch.pipeline().addLast(RegisterRequestHandler.INSTANCE);
                        ch.pipeline().addLast(LoginRequestHandler.INSTANCE);
                        ch.pipeline().addLast(HeartbeatRequestHandler.INSTANCE);
                        ch.pipeline().addLast(AuthHandler.INSTANCE);
                        ch.pipeline().addLast(IMHandler.INSTANCE);
                    }
                });

        // serverBootstrap.bind(8000);
        bind(serverBootstrap, BEGIN_PORT);
        count(serverBootstrap);

    }

    // bind()方法是异步的，我们可以实现端口递增绑定
    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口[" + port + "]绑定成功！");
            } else {
                System.out.println("端口[" + port + "]绑定失败！");
                bind(serverBootstrap, port + 1);
            }
        });
    }

    private static void count(final ServerBootstrap serverBootstrap) {
        System.out.println(new Date() + " 当前连接：" + tcpCount +  " 在线用户：" + userCount);
        serverBootstrap.config().group().schedule(() -> count(serverBootstrap), 5, TimeUnit.SECONDS);
    }
}