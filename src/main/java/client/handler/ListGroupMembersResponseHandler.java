package client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.ListGroupMembersResponsePacket;

import java.util.List;
import java.util.StringJoiner;

/**
 * @author jmx
 * @date 2020/3/10 11:12 PM
 */
public class ListGroupMembersResponseHandler extends SimpleChannelInboundHandler<ListGroupMembersResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersResponsePacket listGroupMembersResponsePacket) {

        String groupId = listGroupMembersResponsePacket.getGroupId();
        String groupName = listGroupMembersResponsePacket.getGroupName();
        List<String> onlineUsers = listGroupMembersResponsePacket.getOnlineUsers();
        List<String> offlineUsers = listGroupMembersResponsePacket.getOfflineUsers();
        int onlineSize = onlineUsers.size();
        int totalSize = offlineUsers.size() + onlineSize;
        System.out.println("群【" + groupId + ":" + groupName + "】的成员如下" +
                "（在线" + onlineSize + "/" + totalSize + "）");

        System.out.println("在线用户");
        String delimeter = "----------";

        System.out.println(delimeter);
        for (String str : onlineUsers) {
            System.out.println(str);
        }
        System.out.println(delimeter);

        System.out.println("离线用户");
        System.out.println(delimeter);
        for (String str : offlineUsers) {
            System.out.println(str);
        }
        System.out.println(delimeter);

    }
}
