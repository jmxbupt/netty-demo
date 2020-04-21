package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.ListContactsRequestPacket;
import protocol.response.ListContactsResponsePacket;
import util.JDBCUtil;
import util.SessionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jmx
 * @date 2020/4/19 5:02 PM
 */
@ChannelHandler.Sharable
public class ListContactsRequestHandler extends SimpleChannelInboundHandler<ListContactsRequestPacket> {

    public static final ListContactsRequestHandler INSTANCE = new ListContactsRequestHandler();

    private ListContactsRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListContactsRequestPacket listContactsRequestPacket) {

        String userId = SessionUtil.getSession(ctx.channel()).getUserId();
        ListContactsResponsePacket listContactsResponsePacket = new ListContactsResponsePacket();

        List<String> contactAsks = new ArrayList<>();
        List<String> onlineContacts = new ArrayList<>();
        List<String> offlineContacts = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBCUtil.JDBC_URL, JDBCUtil.JDBC_USER, JDBCUtil.JDBC_PASSWORD)) {
            // 查询未处理的加好友请求
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT c.user_id, c.ask_content, u.name " +
                            "FROM contactAsks c " +
                            "INNER JOIN users u " +
                            "ON c.user_id = u.id " +
                            "WHERE c.contact_id = ? AND valid = TRUE")) {
                ps.setObject(1, Long.valueOf(userId));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String userId1 = rs.getLong("user_id") + "";
                        String userName1 = rs.getString("name");
                        String content = rs.getString("ask_content");
                        contactAsks.add("[" + userId1 + ":" + userName1 + "]发出的加好友请求：" + content);
                    }
                }
            }
            // 查询好友
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT c.contact_id, u.name, u.online " +
                            "FROM contacts c " +
                            "INNER JOIN users u " +
                            "ON c.contact_id = u.id " +
                            "WHERE c.user_id = ?")) {
                ps.setObject(1, Long.valueOf(userId));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String contactId = rs.getLong("contact_id") + "";
                        String contactName = rs.getString("name");
                        boolean online = rs.getBoolean("online");
                        if (online) {
                            onlineContacts.add("[" + contactId + ":" + contactName + "]");
                        } else {
                            offlineContacts.add("[" + contactId + ":" + contactName + "]");
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        listContactsResponsePacket.setContactAsks(contactAsks);
        listContactsResponsePacket.setOnlineContacts(onlineContacts);
        listContactsResponsePacket.setOfflineContacts(offlineContacts);
        ctx.writeAndFlush(listContactsResponsePacket);
    }
}
