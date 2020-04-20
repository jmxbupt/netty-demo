package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import protocol.request.QuitGroupRequestPacket;
import protocol.response.QuitGroupResponsePacket;
import util.SessionUtil;

/**
 * @author jmx
 * @date 2020/3/10 9:58 PM
 */

@ChannelHandler.Sharable
public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestPacket> {

    public static final QuitGroupRequestHandler INSTANCE = new QuitGroupRequestHandler();

    private QuitGroupRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupRequestPacket quitGroupRequestPacket) {

        QuitGroupResponsePacket quitGroupResponsePacket = new QuitGroupResponsePacket();

        String groupId = quitGroupRequestPacket.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);

        if (channelGroup == null) {
            quitGroupResponsePacket.setSuccess(false);
            quitGroupResponsePacket.setReason("群[" + groupId + "]不存在！");
            ctx.writeAndFlush(quitGroupResponsePacket);
            return;
        }

        if (!channelGroup.contains(SessionUtil.getChannel(SessionUtil.getSession(ctx.channel()).getUserId()))) {
            quitGroupResponsePacket.setSuccess(false);
            quitGroupResponsePacket.setReason("您不在群[" + groupId + "]中");
            ctx.writeAndFlush(quitGroupResponsePacket);
            return;
        }

        String userId = SessionUtil.getSession(ctx.channel()).getUserId();
        SessionUtil.getGroupIds(userId).remove(groupId);

        quitGroupResponsePacket.setSuccess(true);
        quitGroupResponsePacket.setGroupId(groupId);
        // 在客户端显示消息的时候区分退出成员和其他成员
        quitGroupResponsePacket.setSession(SessionUtil.getSession(ctx.channel()));
        channelGroup.writeAndFlush(quitGroupResponsePacket);

        channelGroup.remove(ctx.channel());
        if (channelGroup.size() == 0) {
            SessionUtil.unbindChannelGroup(groupId);
        }
    }
}
