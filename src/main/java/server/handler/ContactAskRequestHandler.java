package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.ContactAskRequestPacket;
import protocol.response.ContactAskResponsePacket;
import session.Session;
import util.JDBCUtil;
import util.SessionUtil;

import java.sql.*;
import java.util.Date;

/**
 * @author jmx
 * @date 2020/4/19 5:01 PM
 */
@ChannelHandler.Sharable
public class ContactAskRequestHandler extends SimpleChannelInboundHandler<ContactAskRequestPacket> {

    public static final ContactAskRequestHandler INSTANCE = new ContactAskRequestHandler();

    private ContactAskRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ContactAskRequestPacket contactAskRequestPacket) {

        Session session = SessionUtil.getSession(ctx.channel());

        String user_id = session.getUserId();
        String contact_id = contactAskRequestPacket.getContactId();
        String content = contactAskRequestPacket.getContent();


        // 处理contactAsks表，需要先查询之前是否请求过
        // 因为有可能是删除好友之后再重新请求，这样处理是为了
        try (Connection conn = DriverManager.getConnection(JDBCUtil.JDBC_URL, JDBCUtil.JDBC_USER, JDBCUtil.JDBC_PASSWORD)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT id FROM contactAsks WHERE user_id = ? AND contact_id = ?")) {
                ps.setObject(1, Long.valueOf(user_id));
                ps.setObject(2, Long.valueOf(contact_id));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("之前请求过加好友，重新请求...");
                        Long id = rs.getLong("id");
                        try (PreparedStatement ps1 = conn.prepareStatement(
                                "UPDATE contactAsks SET ask_time = ?, ask_content = ?, valid = TRUE WHERE id = ?")) {
                            ps1.setObject(1, new Timestamp(new Date().getTime()));
                            ps1.setObject(2, contactAskRequestPacket.getContent());
                            ps1.setObject(3, id);
                            ps1.executeUpdate();
                        }
                    } else {
                        System.out.println("请求加好友...");
                        try (PreparedStatement ps1 = conn.prepareStatement(
                                "INSERT INTO contactAsks (user_id, contact_id, ask_content) VALUES (?, ?, ?)")) {
                            ps1.setObject(1, user_id);
                            ps1.setObject(2, contact_id);
                            ps1.setObject(3, content);
                            ps1.executeUpdate();
                        }
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 暂时不考虑不在线的情况
        Channel channel = SessionUtil.getChannel(contact_id);
        if (channel != null && SessionUtil.hasLogin(channel)) {
            channel.writeAndFlush(new ContactAskResponsePacket());
        }
    }
}
