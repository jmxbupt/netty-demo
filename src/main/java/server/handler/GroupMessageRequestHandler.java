package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.GroupMessageRequestPacket;
import protocol.response.GroupMessageResponsePacket;
import session.Session;
import util.JDBCUtil;
import util.SessionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jmx
 * @date 2020/3/11 12:04 PM
 */

@ChannelHandler.Sharable
public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {

    public static final GroupMessageRequestHandler INSTANCE = new GroupMessageRequestHandler();

    private GroupMessageRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket groupMessageRequestPacket) {

        Session session = SessionUtil.getSession(ctx.channel());
        String userId = session.getUserId();
        String userName = session.getUserName();
        String groupId = groupMessageRequestPacket.getGroupId();
        String content = groupMessageRequestPacket.getContent();

        GroupMessageResponsePacket groupMessageResponsePacket = new GroupMessageResponsePacket();
        groupMessageResponsePacket.setUserId(userId);
        groupMessageResponsePacket.setUserName(userName);
        groupMessageResponsePacket.setGroupId(groupId);
        groupMessageResponsePacket.setContent(content);

        List<String> userIds = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBCUtil.JDBC_URL, JDBCUtil.JDBC_USER, JDBCUtil.JDBC_PASSWORD)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT name FROM groups WHERE id = ?")) {
                ps.setObject(1, Long.valueOf(groupId));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String groupName = rs.getString("name");
                        groupMessageResponsePacket.setGroupName(groupName);
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO groupMessages (group_id, user_id, content) VALUES (?, ?, ?)")) {
                ps.setObject(1, Long.valueOf(groupId));
                ps.setObject(2, Long.valueOf(userId));
                ps.setObject(3, content);
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
                channel.writeAndFlush(groupMessageResponsePacket);
            }
        }
    }
}
