package client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.ListGroupsResponsePacket;

import java.util.StringJoiner;

/**
 * @author jmx
 * @date 2020/4/20 8:42 PM
 */
public class ListGroupsResponseHandler extends SimpleChannelInboundHandler<ListGroupsResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupsResponsePacket listGroupsResponsePacket) {

        System.out.println("群列表");
        String delimeter = "----------";
        StringJoiner sj = new StringJoiner("\n", delimeter + "\n", "\n" + delimeter);
        for (String str : listGroupsResponsePacket.getGroups()) {
            sj.add(str);
        }
        System.out.println(sj.toString());
    }
}
