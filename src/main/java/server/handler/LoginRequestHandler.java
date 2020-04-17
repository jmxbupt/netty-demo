package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.LoginRequestPacket;
import protocol.response.LoginResponsePacket;
import session.Session;
import util.JDBCUtil;
import util.SessionUtil;

import java.sql.*;
import java.util.Date;

/**
 * @author jmx
 * @date 2020/3/10 12:33 PM
 */

// SimpleChannelInboundHandler 帮我们实现了类型判断和对象传递
// 如果类型符合，自动执行channelRead的时候就会调用我们重写的channelRead0()方法
// 如果类型不符合，自动执行channelRead的时候就会调用ctx.fireChannelRead(msg)，跳过该handler
@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    public static final LoginRequestHandler INSTANCE = new LoginRequestHandler();

    private LoginRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) {

        System.out.println(new Date() + "：收到客户端登录请求...");

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setUserName(loginRequestPacket.getUserName());

        if (valid(loginRequestPacket, loginResponsePacket)) {
            // 校验成功
            loginResponsePacket.setSuccess(true);
            System.out.println("[" + loginRequestPacket.getUserName() +  "]登录成功！");
            SessionUtil.bindSession(new Session(loginResponsePacket.getUserId(), loginRequestPacket.getUserName()), ctx.channel());
        } else {
            // 校验失败
            loginResponsePacket.setSuccess(false);
            loginResponsePacket.setReason("账号密码校验失败");
            System.out.println(new Date() + ": 登录失败！");
        }

        ctx.writeAndFlush(loginResponsePacket);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket, LoginResponsePacket loginResponsePacket) {

        String name = loginRequestPacket.getUserName();
        String pwd = loginRequestPacket.getPassword();
        try (Connection conn = DriverManager.getConnection(JDBCUtil.JDBC_URL, JDBCUtil.JDBC_USER, JDBCUtil.JDBC_PASSWORD)) {
            System.out.println("成功建立JDBC连接...");
            try (Statement stmt = conn.createStatement()) {
                System.out.println("SELECT id FROM users WHERE name = '" + name + "' AND pwd = '" + pwd + "'");
                try (ResultSet rs = stmt.executeQuery("SELECT id FROM users WHERE name = '" + name + "' AND pwd = '" + pwd + "'" )) {
                    if (rs.next()) {
                        System.out.println("查询成功！");
                        loginResponsePacket.setUserId(rs.getInt(1) + "");
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionUtil.unBindSession(ctx.channel());
    }
}
