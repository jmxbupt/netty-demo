package client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.ContactConfirmResponsePacket;

/**
 * @author jmx
 * @date 2020/4/20 7:43 AM
 */
public class ContactConfirmResponseHandler extends SimpleChannelInboundHandler<ContactConfirmResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ContactConfirmResponsePacket contactConfirmResponsePacket) {
        System.out.println("成功添加好友，请注意查看...");
    }
}
