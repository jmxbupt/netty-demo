package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import protocol.request.CreateGroupRequestPacket;
import protocol.response.CreateGroupResponsePacket;
import session.Session;
import util.IDUtil;
import util.JDBCUtil;
import util.SessionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jmx
 * @date 2020/3/10 8:18 PM
 */

@ChannelHandler.Sharable
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    public static final CreateGroupRequestHandler INSTANCE = new CreateGroupRequestHandler();

    private CreateGroupRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket createGroupRequestPacket) {

        System.out.println(new Date() + "：收到客户端建群请求...");

        Session session = SessionUtil.getSession(ctx.channel());
        List<String> contactIds = createGroupRequestPacket.getContactIds();
        // 这里将userId加入contactIds中，方便往群成员表中插入记录，以及发送响应
        contactIds.add(session.getUserId());

        CreateGroupResponsePacket createGroupResponsePacket = new CreateGroupResponsePacket();
        createGroupResponsePacket.setUserId(session.getUserId());
        createGroupResponsePacket.setUserName(session.getUserName());

        if (successCreate(createGroupRequestPacket, createGroupResponsePacket, contactIds)) {
            createGroupResponsePacket.setSuccess(true);
            // 发送响应
            for (String contactId : contactIds) {
                Channel channel = SessionUtil.getChannel(contactId);
                if (channel != null && SessionUtil.hasLogin(channel)) {
                    channel.writeAndFlush(createGroupResponsePacket);
                }
            }
            System.out.println(new Date() + "：群【" + createGroupRequestPacket.getGroupName() +  "】建立成功！");
        } else {
            createGroupResponsePacket.setSuccess(false);
            createGroupResponsePacket.setReason("群名已存在！");
            ctx.writeAndFlush(createGroupResponsePacket);
            System.out.println(new Date() + "：群【" + createGroupRequestPacket.getGroupName() +  "】建立失败！");
        }
    }

    private boolean successCreate(CreateGroupRequestPacket createGroupRequestPacket,
                                  CreateGroupResponsePacket createGroupResponsePacket,
                                  List<String> contactIds) {

        String groupName = createGroupRequestPacket.getGroupName();
        createGroupResponsePacket.setGroupName(groupName);

        try (Connection conn = DriverManager.getConnection(JDBCUtil.JDBC_URL, JDBCUtil.JDBC_USER, JDBCUtil.JDBC_PASSWORD)) {
            // 先查询群名是否存在
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM groups WHERE name = ?")) {
                ps.setObject(1, groupName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return false;
                    }
                }
            }
            // 不存在的话再进行插入操作
            // 在群信息表中插入1条记录
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO groups (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setObject(1, groupName);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        long group_id = rs.getLong(1);
                        // 在群成员表中插入若干条记录
                        for (String contactId : contactIds) {
                            try (PreparedStatement ps1 = conn.prepareStatement(
                                    "INSERT INTO group2user (group_id, user_id) VALUES (?, ?)")) {
                                ps1.setObject(1, group_id);
                                ps1.setObject(2, Long.valueOf(contactId));
                                ps1.executeUpdate();
                            }
                        }
                        return true;
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
