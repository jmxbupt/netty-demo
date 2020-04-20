package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.RegisterRequestPacket;
import protocol.response.RegisterResponsePacket;
import server.NettyServer;
import session.Session;
import util.JDBCUtil;
import util.SessionUtil;

import java.sql.*;
import java.util.Date;

/**
 * @author jmx
 * @date 2020/4/17 4:35 PM
 */
@ChannelHandler.Sharable
public class RegisterRequestHandler extends SimpleChannelInboundHandler<RegisterRequestPacket> {

    public static final RegisterRequestHandler INSTANCE = new RegisterRequestHandler();

    private RegisterRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RegisterRequestPacket registerRequestPacket) {

        System.out.println(new Date() + "：收到客户端注册请求...");
        String userName = registerRequestPacket.getUserName();
        RegisterResponsePacket registerResponsePacket = new RegisterResponsePacket();
        registerResponsePacket.setUserName(userName);

        if (successRegisterd(registerRequestPacket, registerResponsePacket)) {
            registerResponsePacket.setSuccess(true);
            System.out.println("[" + userName + "]注册成功！");
            SessionUtil.bindSession(new Session(registerResponsePacket.getUserId(), userName), ctx.channel());
            NettyServer.userCount.incrementAndGet();
        } else {
            registerResponsePacket.setSuccess(false);
            registerResponsePacket.setReason("用户名已存在！");
            System.out.println(new Date() + ": 注册失败！");
        }
        ctx.writeAndFlush(registerResponsePacket);
    }

    private boolean successRegisterd(RegisterRequestPacket registerRequestPacket, RegisterResponsePacket registerResponsePacket) {

        String name = registerRequestPacket.getUserName();
        String pwd = registerRequestPacket.getPassword();
        try (Connection conn = DriverManager.getConnection(JDBCUtil.JDBC_URL, JDBCUtil.JDBC_USER, JDBCUtil.JDBC_PASSWORD)) {
            // 先查询用户名是否存在
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE name = ?")) {
                ps.setObject(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return false;
                    }
                }
            }
            // 再进行插入操作
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO users (name, pwd) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setObject(1, name);
                ps.setObject(2, pwd);
                int n = ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        registerResponsePacket.setUserId(rs.getLong(1) + "");
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
