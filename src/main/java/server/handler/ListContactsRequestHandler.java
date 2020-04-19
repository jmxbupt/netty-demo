package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.ListContactsRequestPacket;

/**
 * @author jmx
 * @date 2020/4/19 5:02 PM
 */
@ChannelHandler.Sharable
public class ListContactsRequestHandler extends SimpleChannelInboundHandler<ListContactsRequestPacket> {

    public static final ListContactsRequestHandler INSTANCE = new ListContactsRequestHandler();

    private ListContactsRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListContactsRequestPacket listContactsRequestPacket) {

    }
}
