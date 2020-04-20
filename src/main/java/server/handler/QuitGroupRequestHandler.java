package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.QuitGroupRequestPacket;
import protocol.response.QuitGroupResponsePacket;
import session.Session;
import util.JDBCUtil;
import util.SessionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jmx
 * @date 2020/3/10 9:58 PM
 */

@ChannelHandler.Sharable
public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestPacket> {

    public static final QuitGroupRequestHandler INSTANCE = new QuitGroupRequestHandler();

    private QuitGroupRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupRequestPacket quitGroupRequestPacket) {

        Session session = SessionUtil.getSession(ctx.channel());
        String userId = session.getUserId();
        String userName = session.getUserName();
        String groupId = quitGroupRequestPacket.getGroupId();

        QuitGroupResponsePacket quitGroupResponsePacket = new QuitGroupResponsePacket();
        quitGroupResponsePacket.setUserId(userId);
        quitGroupResponsePacket.setUserName(userName);
        quitGroupResponsePacket.setGroupId(groupId);

        List<String> userIds = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBCUtil.JDBC_URL, JDBCUtil.JDBC_USER, JDBCUtil.JDBC_PASSWORD)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT name FROM groups WHERE id = ?")) {
                ps.setObject(1, Long.valueOf(groupId));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String groupName = rs.getString("name");
                        quitGroupResponsePacket.setGroupName(groupName);
                    }
                }
            }
            // 先查询后删除，和加群操作不一样
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT user_id FROM group2user WHERE group_id = ?")) {
                ps.setObject(1, Long.valueOf(groupId));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        userIds.add(rs.getLong("user_id") + "");
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM group2user WHERE group_id = ? AND user_id = ?")) {
                ps.setObject(1, Long.valueOf(groupId));
                ps.setObject(2, Long.valueOf(userId));
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String userId1 : userIds) {
            Channel channel = SessionUtil.getChannel(userId1);
            if (channel != null && SessionUtil.hasLogin(channel)) {
                channel.writeAndFlush(quitGroupResponsePacket);
            }
        }
    }
}
