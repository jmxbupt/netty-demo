package client.handler;

import client.console.ConsoleCommandManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.QuitGroupResponsePacket;

/**
 * @author jmx
 * @date 2020/3/10 11:13 PM
 */
public class QuitGroupResponseHandler extends SimpleChannelInboundHandler<QuitGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupResponsePacket quitGroupResponsePacket) {

        String userId = quitGroupResponsePacket.getUserId();
        String userName = quitGroupResponsePacket.getUserName();
        String groupId = quitGroupResponsePacket.getGroupId();
        String groupName = quitGroupResponsePacket.getGroupName();

        if (ConsoleCommandManager.userId.equals(userId)) {
            System.out.println("您已退出群【" + groupId + ":" + groupName + "】");
        } else {
            System.out.println("[" + userId + ":" + userName + "]退出了群【" + groupId + ":" + groupName + "】");
        }
        
    }
}
