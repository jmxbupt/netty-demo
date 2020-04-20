package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import protocol.request.GroupMessageRequestPacket;
import protocol.response.GroupMessageResponsePacket;
import util.SessionUtil;

/**
 * @author jmx
 * @date 2020/3/11 12:04 PM
 */

@ChannelHandler.Sharable
public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {

    public static final GroupMessageRequestHandler INSTANCE = new GroupMessageRequestHandler();

    private GroupMessageRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket groupMessageRequestPacket) {

        GroupMessageResponsePacket groupMessageResponsePacket = new GroupMessageResponsePacket();

        String groupId = groupMessageRequestPacket.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);

        if (channelGroup == null) {
            groupMessageResponsePacket.setSuccess(false);
            groupMessageResponsePacket.setReason("群[" + groupId + "]不存在！");
            ctx.writeAndFlush(groupMessageResponsePacket);
            return;
        }

        // 下面这种情况在UI界面是不会发生的
        if (!channelGroup.contains(SessionUtil.getChannel(SessionUtil.getSession(ctx.channel()).getUserId()))) {
            groupMessageResponsePacket.setSuccess(false);
            groupMessageResponsePacket.setReason("您不在群[" + groupId + "]中");
            ctx.writeAndFlush(groupMessageResponsePacket);
            return;
        }

        groupMessageResponsePacket.setSuccess(true);
        groupMessageResponsePacket.setFromGroupId(groupId);
        groupMessageResponsePacket.setFromUser(SessionUtil.getSession(ctx.channel()));
        groupMessageResponsePacket.setMessage(groupMessageRequestPacket.getMessage());
        channelGroup.writeAndFlush(groupMessageResponsePacket);
    }
}
