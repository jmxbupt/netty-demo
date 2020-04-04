package client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.JoinGroupResponsePacket;
import session.Session;
import util.SessionUtil;

/**
 * @author jmx
 * @date 2020/3/10 11:13 PM
 */
public class JoinGroupResponseHandler extends SimpleChannelInboundHandler<JoinGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupResponsePacket joinGroupResponsePacket) {

        if (!joinGroupResponsePacket.isSuccess()) {
            System.out.println(joinGroupResponsePacket.getReason());
        } else {
            Session session = joinGroupResponsePacket.getSession();
            if (SessionUtil.getSession(ctx.channel()).getUserId().equals(session.getUserId())) {
                System.out.println("您已加入群[" + joinGroupResponsePacket.getGroupId() + "]");
            } else {
                System.out.println(session.getUserId() + ":" + session.getUserName()
                        + "加入了群[" + joinGroupResponsePacket.getGroupId() + "]");
            }
        }

    }
}
