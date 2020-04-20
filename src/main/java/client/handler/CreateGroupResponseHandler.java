package client.handler;

import client.console.ConsoleCommandManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.CreateGroupResponsePacket;
import session.Session;

import java.util.List;

/**
 * @author jmx
 * @date 2020/3/10 8:19 PM
 */
public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponsePacket createGroupResponsePacket) {

        if (!createGroupResponsePacket.isSuccess()) {
            System.out.println(createGroupResponsePacket.getReason());
        } else {
            Session session = createGroupResponsePacket.getSession();
            List<Session> sessionList = createGroupResponsePacket.getSessionList();
            if (ConsoleCommandManager.userId.equals(session.getUserId())) {
                System.out.print("群[" + createGroupResponsePacket.getGroupId() + "]创建成功，");
            } else {
                System.out.print(session.getUserId() + ":" + session.getUserName()
                        + " 创建了群[" + createGroupResponsePacket.getGroupId() + "]，");
            }
            System.out.println("群成员有：" + sessionList);
        }
    }
}
