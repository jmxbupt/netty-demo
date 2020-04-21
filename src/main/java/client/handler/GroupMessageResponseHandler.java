package client.handler;

import client.console.ConsoleCommandManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.GroupMessageResponsePacket;

import java.util.Date;

/**
 * @author jmx
 * @date 2020/3/11 12:05 PM
 */
public class GroupMessageResponseHandler extends SimpleChannelInboundHandler<GroupMessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageResponsePacket groupMessageResponsePacket) {

        String userId = groupMessageResponsePacket.getUserId();
        String userName = groupMessageResponsePacket.getUserName();
        String groupId = groupMessageResponsePacket.getGroupId();
        String groupName = groupMessageResponsePacket.getGroupName();
        String content = groupMessageResponsePacket.getContent();

        if (ConsoleCommandManager.userId.equals(userId)) {
            System.out.println(new Date());
            System.out.println("您在群【" + groupId + ":" + groupName + "】中发送了消息：" + content);
        } else {
            System.out.println(new Date());
            System.out.println("收到群【" + groupId + ":" + groupName + "】中的[" + userId + ":" + userName + "]发送的消息：" + content);
        }
    }
}
