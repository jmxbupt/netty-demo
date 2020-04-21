package client.handler;

import client.console.ConsoleCommandManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.MessageResponsePacket;

import java.util.Date;

/**
 * @author jmx
 * @date 2020/3/10 1:10 PM
 */
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) {

        String userId = messageResponsePacket.getUserId();
        String userName = messageResponsePacket.getUserName();
        String contactId = messageResponsePacket.getContactId();
        String contactName = messageResponsePacket.getContactName();
        String content = messageResponsePacket.getContent();

        if (ConsoleCommandManager.userId.equals(userId)) {
            System.out.println(new Date());
            System.out.println("您给[" + contactId + ":" + contactName + "]发送了消息： " + content);
        } else {
            System.out.println(new Date());
            System.out.println("收到[" + userId + ":" + userName + "]发送的消息：" + content);
        }
    }
}
