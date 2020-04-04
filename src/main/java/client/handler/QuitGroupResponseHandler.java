package client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.QuitGroupResponsePacket;
import session.Session;
import util.SessionUtil;

/**
 * @author jmx
 * @date 2020/3/10 11:13 PM
 */
public class QuitGroupResponseHandler extends SimpleChannelInboundHandler<QuitGroupResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupResponsePacket quitGroupResponsePacket) {

        if (!quitGroupResponsePacket.isSuccess()) {
            System.out.println(quitGroupResponsePacket.getReason());
        } else {
            Session session = quitGroupResponsePacket.getSession();
            if (SessionUtil.getSession(ctx.channel()).getUserId().equals(session.getUserId())) {
                System.out.println("您已退出群[" + quitGroupResponsePacket.getGroupId() + "]");
            } else {
                System.out.println(session.getUserId() + ":" + session.getUserName()
                        + "退出了群[" + quitGroupResponsePacket.getGroupId() + "]");
            }
        }
    }
}
