package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import protocol.request.ListGroupMembersRequestPacket;
import protocol.response.ListGroupMembersResponsePacket;
import session.Session;
import util.SessionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jmx
 * @date 2020/3/10 9:56 PM
 */

@ChannelHandler.Sharable
public class ListGroupMembersRequestHandler extends SimpleChannelInboundHandler<ListGroupMembersRequestPacket> {

    public static final ListGroupMembersRequestHandler INSTANCE = new ListGroupMembersRequestHandler();

    private ListGroupMembersRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersRequestPacket listGroupMembersRequestPacket) {

        ListGroupMembersResponsePacket listGroupMembersResponsePacket = new ListGroupMembersResponsePacket();

        String groupId = listGroupMembersRequestPacket.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);

        if (channelGroup == null) {
            listGroupMembersResponsePacket.setSuccess(false);
            listGroupMembersResponsePacket.setReason("查询的群不存在！");
            ctx.writeAndFlush(listGroupMembersResponsePacket);
            return;
        }

        List<Session> sessionList = new ArrayList<>();
        for (Channel channel: channelGroup) {
            sessionList.add(SessionUtil.getSession(channel));
        }

        listGroupMembersResponsePacket.setSuccess(true);
        listGroupMembersResponsePacket.setGroupId(groupId);
        listGroupMembersResponsePacket.setSessionList(sessionList);

        ctx.writeAndFlush(listGroupMembersResponsePacket);
    }
}
