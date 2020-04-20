package client.handler;

import client.console.ConsoleCommandManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.CreateGroupResponsePacket;


/**
 * @author jmx
 * @date 2020/3/10 8:19 PM
 */
public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponsePacket createGroupResponsePacket) {

        String groupName = createGroupResponsePacket.getGroupName();

        if (!createGroupResponsePacket.isSuccess()) {
            String reason = createGroupResponsePacket.getReason();
            System.out.println("群【" + groupName +  "】建立失败, 原因：" + reason);
        } else {
            String userId = createGroupResponsePacket.getUserId();
            String userName = createGroupResponsePacket.getUserName();
            if (ConsoleCommandManager.userId.equals(userId)) {
                System.out.println("群【" + groupName +  "】建立成功！");
            } else {
                System.out.println("[" + userId + ":" + userName + "]拉您进入群【" + groupName + "】");
            }
        }
    }
}
