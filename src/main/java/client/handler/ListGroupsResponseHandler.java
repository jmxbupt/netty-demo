package client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.ListGroupsResponsePacket;

/**
 * @author jmx
 * @date 2020/4/20 8:42 PM
 */
public class ListGroupsResponseHandler extends SimpleChannelInboundHandler<ListGroupsResponsePacket> {

    private static final String DELIMETER = "----------";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupsResponsePacket listGroupsResponsePacket) {

        System.out.println("群列表");
        System.out.println(DELIMETER);
        for (String str : listGroupsResponsePacket.getGroups()) {
            System.out.println(str);
        }
        System.out.println(DELIMETER);
    }
}
