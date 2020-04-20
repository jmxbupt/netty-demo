package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.ContactConfirmRequestPacket;
import protocol.response.ContactConfirmResponsePacket;
import session.Session;
import util.JDBCUtil;
import util.SessionUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author jmx
 * @date 2020/4/19 5:01 PM
 */
@ChannelHandler.Sharable
public class ContactConfirmRequestHandler extends SimpleChannelInboundHandler<ContactConfirmRequestPacket> {

    public static final ContactConfirmRequestHandler INSTANCE = new ContactConfirmRequestHandler();

    private ContactConfirmRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ContactConfirmRequestPacket contactConfirmRequestPacket) {

        Session session = SessionUtil.getSession(ctx.channel());

        String user_id = contactConfirmRequestPacket.getUserId();
        String contact_id = session.getUserId();

        try (Connection conn = DriverManager.getConnection(JDBCUtil.JDBC_URL, JDBCUtil.JDBC_USER, JDBCUtil.JDBC_PASSWORD)) {
            // 更新contactAsks表
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE contactAsks SET valid = FALSE WHERE user_id = ? AND contact_id = ?")) {
                ps.setObject(1, Long.valueOf(user_id));
                ps.setObject(2, Long.valueOf(contact_id));
                ps.executeUpdate();
            }
            // 在contacts表中插入两条记录
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO contacts (user_id, contact_id) VALUES (?, ?), (?, ?)")) {
                ps.setObject(1, Long.valueOf(user_id));
                ps.setObject(2, Long.valueOf(contact_id));

                ps.setObject(3, Long.valueOf(contact_id));
                ps.setObject(4, Long.valueOf(user_id));

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ContactConfirmResponsePacket contactConfirmResponsePacket = new ContactConfirmResponsePacket();
        // 给确认方发响应
        ctx.writeAndFlush(contactConfirmResponsePacket);
        // 给请求方发响应
        Channel channel = SessionUtil.getChannel(user_id);
        if (channel != null && SessionUtil.hasLogin(channel)) {
            channel.writeAndFlush(contactConfirmResponsePacket);
        }

    }
}
