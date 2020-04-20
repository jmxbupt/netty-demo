package client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.ContactAskResponsePacket;

/**
 * @author jmx
 * @date 2020/4/19 10:24 PM
 */
public class ContackAskResponseHandler extends SimpleChannelInboundHandler<ContactAskResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ContactAskResponsePacket contactAskResponsePacket) {

        System.out.println("收到加好友请求，请注意查看...");
    }
}
