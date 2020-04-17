package client.handler;

import client.console.ConsoleCommandManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.GroupMessageResponsePacket;
import session.Session;

/**
 * @author jmx
 * @date 2020/3/11 12:05 PM
 */
public class GroupMessageResponseHandler extends SimpleChannelInboundHandler<GroupMessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageResponsePacket groupMessageResponsePacket) {

        if (!groupMessageResponsePacket.isSuccess()) {
            System.out.println(groupMessageResponsePacket.getReason());
        } else {
            Session session = groupMessageResponsePacket.getFromUser();
            if (ConsoleCommandManager.userId.equals(session.getUserId())) {
                System.out.println("群消息发送成功！");
            } else {
                System.out.println("收到群[" + groupMessageResponsePacket.getFromGroupId() + "]中["
                        + session + "]发来的消息：" + groupMessageResponsePacket.getMessage());
            }
        }
    }
}
