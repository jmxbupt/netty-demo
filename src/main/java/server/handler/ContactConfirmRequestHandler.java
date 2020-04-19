package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.ContactConfirmRequestPacket;

/**
 * @author jmx
 * @date 2020/4/19 5:01 PM
 */
@ChannelHandler.Sharable
public class ContactConfirmRequestHandler extends SimpleChannelInboundHandler<ContactConfirmRequestPacket> {

    public static final ContactConfirmRequestHandler INSTANCE = new ContactConfirmRequestHandler();

    private ContactConfirmRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ContactConfirmRequestPacket contactConfirmRequestPacket) {

    }
}
