package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.MessageRequestPacket;
import protocol.response.MessageResponsePacket;
import session.Session;
import util.SessionUtil;

/**
 * @author jmx
 * @date 2020/3/10 12:48 PM
 */

@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();

    private MessageRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) {

        // 拿到消息发送方的会话信息
        Session session = SessionUtil.getSession(ctx.channel());

        // 通过消息发送方的会话信息构造要发送的消息
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(session.getUserId());
        messageResponsePacket.setFromUserName(session.getUserName());
        messageResponsePacket.setMessage(messageRequestPacket.getMessage());

        // 拿到消息接收方的channel
        Channel toUserChannel = SessionUtil.getChannel(messageRequestPacket.getToUserId());

        // 如果消息接收方在线，则发送给消息接收方，否则标记为false发回消息发送方
        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)) {
            messageResponsePacket.setSuccess(true);
            toUserChannel.writeAndFlush(messageResponsePacket);
        } else {
            messageResponsePacket.setSuccess(false);
            messageResponsePacket.setReason("[" + messageRequestPacket.getToUserId() + "] 不在线，发送失败!");
            ctx.writeAndFlush(messageResponsePacket);
        }
    }
}
