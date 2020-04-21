package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.ContactDeleteRequestPacket;
import protocol.response.ContactDeleteResponsePacket;
import session.Session;
import util.JDBCUtil;
import util.SessionUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author jmx
 * @date 2020/4/20 11:10 AM
 */
@ChannelHandler.Sharable
public class ContactDeleteRequestHandler extends SimpleChannelInboundHandler<ContactDeleteRequestPacket> {

    public static final ContactDeleteRequestHandler INSTANCE = new ContactDeleteRequestHandler();

    private ContactDeleteRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ContactDeleteRequestPacket contactDeleteRequestPacket) {

        Session session = SessionUtil.getSession(ctx.channel());

        String userId = session.getUserId();
        String contactId = contactDeleteRequestPacket.getContactId();

        try (Connection conn = DriverManager.getConnection(JDBCUtil.JDBC_URL, JDBCUtil.JDBC_USER, JDBCUtil.JDBC_PASSWORD)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM contacts WHERE user_id = ? AND contact_id = ? OR user_id = ? AND contact_id = ? ")) {
                ps.setObject(1, Long.valueOf(userId));
                ps.setObject(2, Long.valueOf(contactId));
                ps.setObject(3, Long.valueOf(contactId));
                ps.setObject(4, Long.valueOf(userId));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ContactDeleteResponsePacket contactDeleteResponsePacket = new ContactDeleteResponsePacket();
        // 暂定不通知对方
        ctx.writeAndFlush(contactDeleteResponsePacket);
    }
}
