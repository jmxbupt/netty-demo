package client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.ContactDeleteResponsePacket;

/**
 * @author jmx
 * @date 2020/4/20 11:11 AM
 */
public class ContactDeleteResponseHandler extends SimpleChannelInboundHandler<ContactDeleteResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ContactDeleteResponsePacket contactDeleteResponsePacket) {

        System.out.println("成功删除好友...");
    }
}
