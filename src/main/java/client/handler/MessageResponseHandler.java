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

        if (!messageResponsePacket.isSuccess()) {
            System.out.println(messageResponsePacket.getReason());
        } else {
            String fromUserId = messageResponsePacket.getFromUserId();
            String fromUserName = messageResponsePacket.getFromUserName();
            System.out.println(fromUserId + ":" + fromUserName + " -> " + messageResponsePacket.getMessage());
        }


    }
}
