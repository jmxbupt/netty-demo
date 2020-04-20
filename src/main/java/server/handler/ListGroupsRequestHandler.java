package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.ListGroupsRequestPacket;
import protocol.response.ListGroupsResponsePacket;
import session.Session;
import util.JDBCUtil;
import util.SessionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jmx
 * @date 2020/4/20 8:28 PM
 */
@ChannelHandler.Sharable
public class ListGroupsRequestHandler extends SimpleChannelInboundHandler<ListGroupsRequestPacket> {

    public static final ListGroupsRequestHandler INSTANCE = new ListGroupsRequestHandler();

    private ListGroupsRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupsRequestPacket listGroupsRequestPacket) {

        Session session = SessionUtil.getSession(ctx.channel());
        String userId = session.getUserId();
        ListGroupsResponsePacket listGroupsResponsePacket = new ListGroupsResponsePacket();
        List<String> groups = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(JDBCUtil.JDBC_URL, JDBCUtil.JDBC_USER, JDBCUtil.JDBC_PASSWORD)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT a.group_id, b.name " +
                            "FROM group2user a " +
                            "INNER JOIN groups b " +
                            "ON a.group_id = b.id " +
                            "WHERE a.user_id = ?")) {
                ps.setObject(1, Long.valueOf(userId));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String groupId = rs.getString("group_id");
                        String groupName = rs.getString("name");
                        groups.add("【" + groupId + ":" + groupName + "】");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listGroupsResponsePacket.setGroups(groups);
        ctx.writeAndFlush(listGroupsResponsePacket);
    }
}
