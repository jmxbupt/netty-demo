package client.handler;

import client.console.ConsoleCommandManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.JoinGroupResponsePacket;

/**
 * @author jmx
 * @date 2020/3/10 11:13 PM
 */
public class JoinGroupResponseHandler extends SimpleChannelInboundHandler<JoinGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupResponsePacket joinGroupResponsePacket) {

        String userId = joinGroupResponsePacket.getUserId();
        String userName = joinGroupResponsePacket.getUserName();
        String groupId = joinGroupResponsePacket.getGroupId();
        String groupName = joinGroupResponsePacket.getGroupName();

        if (ConsoleCommandManager.userId.equals(userId)) {
            System.out.println("您已加入群【" + groupId + ":" + groupName + "】");
        } else {
            System.out.println("[" + userId + ":" + userName + "]加入了群【" + groupId + ":" + groupName + "】");
        }
    }
}
