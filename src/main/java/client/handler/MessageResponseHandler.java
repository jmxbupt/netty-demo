package client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.MessageResponsePacket;

/**
 * @author jmx
 * @date 2020/3/10 1:10 PM
 */
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) {

        String userId = messageResponsePacket.getUserId();
        String userName = messageResponsePacket.getUserName();
        String content = messageResponsePacket.getContent();

        System.out.println("[" + userId + ":" + userName + "] " + content);
    }
}
