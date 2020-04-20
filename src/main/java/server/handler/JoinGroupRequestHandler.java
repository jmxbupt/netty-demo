package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.JoinGroupRequestPacket;
import protocol.response.JoinGroupResponsePacket;
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
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {

    public static final JoinGroupRequestHandler INSTANCE = new JoinGroupRequestHandler();

    private JoinGroupRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket joinGroupRequestPacket) {

        Session session = SessionUtil.getSession(ctx.channel());
        String userId = session.getUserId();
        String userName = session.getUserName();
        String groupId = joinGroupRequestPacket.getGroupId();

        JoinGroupResponsePacket joinGroupResponsePacket = new JoinGroupResponsePacket();
        joinGroupResponsePacket.setUserId(userId);
        joinGroupResponsePacket.setUserName(userName);
        joinGroupResponsePacket.setGroupId(groupId);

        List<String> userIds = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBCUtil.JDBC_URL, JDBCUtil.JDBC_USER, JDBCUtil.JDBC_PASSWORD)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT name FROM groups WHERE id = ?")) {
                ps.setObject(1, Long.valueOf(groupId));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String groupName = rs.getString("name");
                        joinGroupResponsePacket.setGroupName(groupName);
                    }
                }
            }
            // 先插入后查询，和退群操作不一样
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO group2user (group_id, user_id) VALUES (?, ?)")) {
                ps.setObject(1, Long.valueOf(groupId));
                ps.setObject(2, Long.valueOf(userId));
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT user_id FROM group2user WHERE group_id = ?")) {
                ps.setObject(1, Long.valueOf(groupId));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        userIds.add(rs.getLong("user_id") + "");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String userId1 : userIds) {
            Channel channel = SessionUtil.getChannel(userId1);
            if (channel != null && SessionUtil.hasLogin(channel)) {
                channel.writeAndFlush(joinGroupResponsePacket);
            }
        }
    }
}
