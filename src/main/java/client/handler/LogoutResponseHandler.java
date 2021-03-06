package client.handler;

import client.console.ConsoleCommandManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.LogoutResponsePacket;

/**
 * @author jmx
 * @date 2020/3/10 8:17 PM
 */
public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket logoutResponsePacket) {

        System.out.println("您已退出登录！");
        ConsoleCommandManager.hasLogin = false;
        ConsoleCommandManager.userId = null;
        ctx.channel().close();
    }
}
