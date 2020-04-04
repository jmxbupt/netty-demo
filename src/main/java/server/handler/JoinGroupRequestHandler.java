package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import protocol.request.JoinGroupRequestPacket;
import protocol.response.JoinGroupResponsePacket;
import util.SessionUtil;

/**
 * @author jmx
 * @date 2020/3/10 9:58 PM
 */

@ChannelHandler.Sharable
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {

    public static final JoinGroupRequestHandler INSTANCE = new JoinGroupRequestHandler();

    private JoinGroupRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket joinGroupRequestPacket) {

        JoinGroupResponsePacket joinGroupResponsePacket = new JoinGroupResponsePacket();

        String groupId = joinGroupRequestPacket.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);

        if (channelGroup == null) {
            joinGroupResponsePacket.setSuccess(false);
            joinGroupResponsePacket.setReason("要加入的群不存在！");
            ctx.writeAndFlush(joinGroupResponsePacket);
            return;
        }

        channelGroup.add(ctx.channel());
        joinGroupResponsePacket.setSuccess(true);
        joinGroupResponsePacket.setSession(SessionUtil.getSession(ctx.channel()));
        joinGroupResponsePacket.setGroupId(groupId);
        // 在客户端显示消息的时候区分新加入成员和其他成员
        channelGroup.writeAndFlush(joinGroupResponsePacket);
    }
}
