package client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.ListGroupMembersResponsePacket;

/**
 * @author jmx
 * @date 2020/3/10 11:12 PM
 */
public class ListGroupMembersResponseHandler extends SimpleChannelInboundHandler<ListGroupMembersResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersResponsePacket listGroupMembersResponsePacket) {

        if (!listGroupMembersResponsePacket.isSuccess()) {
            System.out.println(listGroupMembersResponsePacket.getReason());
        } else {
            System.out.println("群[" + listGroupMembersResponsePacket.getGroupId() + "]的成员有："
                    + listGroupMembersResponsePacket.getSessionList());
        }
    }
}
