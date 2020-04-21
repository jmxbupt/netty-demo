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

        RegisterResponsePacket registerResponsePacket = new RegisterResponsePacket();

        if (successRegister(registerRequestPacket, registerResponsePacket)) {
            registerResponsePacket.setSuccess(true);
            String userId = registerResponsePacket.getUserId();
            String userName = registerResponsePacket.getUserName();
            SessionUtil.bindSession(new Session(userId, userName), ctx.channel());
            NettyServer.userCount.incrementAndGet();
            System.out.println(new Date() + ": [" + userId + ":" + userName + "]注册成功！");
        } else {
            registerResponsePacket.setSuccess(false);
            registerResponsePacket.setReason("用户名已存在！");
            String userName = registerResponsePacket.getUserName();
            System.out.println(new Date() + ": [" + userName + "]注册失败！");
        }
        ctx.writeAndFlush(registerResponsePacket);
    }

    private boolean successRegister(RegisterRequestPacket registerRequestPacket, RegisterResponsePacket registerResponsePacket) {

        String userName = registerRequestPacket.getUserName();
        String password = registerRequestPacket.getPassword();
        registerResponsePacket.setUserName(userName);

        try (Connection conn = DriverManager.getConnection(JDBCUtil.JDBC_URL, JDBCUtil.JDBC_USER, JDBCUtil.JDBC_PASSWORD)) {
            // 先查询用户名是否存在
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE name = ?")) {
                ps.setObject(1, userName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return false;
                    }
                }
            }
            // 不存在的话再进行插入操作
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO users (name, pwd) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setObject(1, userName);
                ps.setObject(2, password);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        // userId将用于bindSession
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
