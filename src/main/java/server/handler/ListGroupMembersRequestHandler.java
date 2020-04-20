package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import protocol.request.ListGroupMembersRequestPacket;
import protocol.response.ListGroupMembersResponsePacket;
import session.Session;
import util.JDBCUtil;
import util.SessionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jmx
 * @date 2020/3/10 9:56 PM
 */

@ChannelHandler.Sharable
public class ListGroupMembersRequestHandler extends SimpleChannelInboundHandler<ListGroupMembersRequestPacket> {

    public static final ListGroupMembersRequestHandler INSTANCE = new ListGroupMembersRequestHandler();

    private ListGroupMembersRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersRequestPacket listGroupMembersRequestPacket) {

        String groupId = listGroupMembersRequestPacket.getGroupId();

        ListGroupMembersResponsePacket listGroupMembersResponsePacket = new ListGroupMembersResponsePacket();
        listGroupMembersResponsePacket.setGroupId(groupId);

        List<String> onlineUsers = new ArrayList<>();
        List<String> offlineUsers = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBCUtil.JDBC_URL, JDBCUtil.JDBC_USER, JDBCUtil.JDBC_PASSWORD)) {
            // 查询groups表拿到groupName
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT name FROM groups WHERE id = ?")) {
                ps.setObject(1, Long.valueOf(groupId));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String groupName = rs.getString("name");
                        listGroupMembersResponsePacket.setGroupName(groupName);
                    }
                }
            }
            // 查询group2user和users表拿到用户记录
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT g.user_id, u.name, u.online " +
                            "FROM group2user g " +
                            "INNER JOIN users u " +
                            "ON g.user_id = u.id " +
                            "WHERE g.group_id = ?")) {
                ps.setObject(1, Long.valueOf(groupId));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String userId = rs.getString("user_id") + "";
                        String userName = rs.getString("name");
                        boolean online = rs.getBoolean("online");
                        if (online) {
                            onlineUsers.add("[" + userId + ":" + userName + "]");
                        } else {
                            offlineUsers.add("[" + userId + ":" + userName + "]");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listGroupMembersResponsePacket.setOnlineUsers(onlineUsers);
        listGroupMembersResponsePacket.setOfflineUsers(offlineUsers);
        ctx.writeAndFlush(listGroupMembersResponsePacket);
    }
}
