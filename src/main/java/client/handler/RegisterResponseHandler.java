package client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.RegisterResponsePacket;
import session.Session;
import util.ClientSessionUtil;

/**
 * @author jmx
 * @date 2020/4/17 4:47 PM
 */
public class RegisterResponseHandler extends SimpleChannelInboundHandler<RegisterResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RegisterResponsePacket registerResponsePacket) {

        String userName = registerResponsePacket.getUserName();

        if (registerResponsePacket.isSuccess()) {
            String userId = registerResponsePacket.getUserId();
            System.out.println("[" + userName + "]登录成功，userId 为: " + userId);
            ClientSessionUtil.bindSession(new Session(userId, userName), ctx.channel());
        } else {
            System.out.println("[" + userName + "]登录失败，原因：" + registerResponsePacket.getReason());
        }
    }
}
