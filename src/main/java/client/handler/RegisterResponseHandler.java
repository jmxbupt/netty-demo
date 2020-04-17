package client.handler;

import client.console.ConsoleCommandManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.RegisterResponsePacket;

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
            System.out.println("[" + userName + "]已成功注册并登录，userId 为: " + userId);
            ConsoleCommandManager.hasLogin = true;
        } else {
            System.out.println("[" + userName + "]注册失败，原因：" + registerResponsePacket.getReason());
        }
    }
}
