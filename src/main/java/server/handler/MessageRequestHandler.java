package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.MessageRequestPacket;
import protocol.response.MessageResponsePacket;
import session.Session;
import util.JDBCUtil;
import util.SessionUtil;

import java.sql.*;

/**
 * @author jmx
 * @date 2020/3/10 12:48 PM
 */

@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();

    private MessageRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) {

        Session session = SessionUtil.getSession(ctx.channel());

        String userId = session.getUserId();
        String userName = session.getUserName();
        String contactId = messageRequestPacket.getContactId();
        String content = messageRequestPacket.getContent();

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setUserId(userId);
        messageResponsePacket.setUserName(userName);
        messageResponsePacket.setContactId(contactId);
        messageResponsePacket.setContent(content);

        try (Connection conn = DriverManager.getConnection(JDBCUtil.JDBC_URL, JDBCUtil.JDBC_USER, JDBCUtil.JDBC_PASSWORD)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO messages (user_id, contact_id, content) VALUES (?, ?, ?)")) {
                ps.setObject(1, Long.valueOf(userId));
                ps.setObject(2, Long.valueOf(contactId));
                ps.setObject(3, content);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT name FROM users WHERE id = ?")) {
                ps.setObject(1, Long.valueOf(contactId));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String contactName = rs.getString("name");
                        messageResponsePacket.setContactName(contactName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ctx.writeAndFlush(messageResponsePacket);
        // 按当前的设计，只有好友在线才能给其发送消息，A看到B在线，给B发了一条消息
        // 但是可能这条消息到了服务器，还没来得及发送给B，B就退出登录了，所以这里还是需要进行判断
        Channel channel = SessionUtil.getChannel(contactId);
        if (channel != null && SessionUtil.hasLogin(channel)) {
            channel.writeAndFlush(messageResponsePacket);
        }
    }
}

