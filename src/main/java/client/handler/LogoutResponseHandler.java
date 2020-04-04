package client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.LogoutResponsePacket;
import util.ClientSessionUtil;

/**
 * @author jmx
 * @date 2020/3/10 8:17 PM
 */
public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket logoutResponsePacket) {
        if (logoutResponsePacket.isSuccess()) {
            System.out.println("您已退出登录！");
            // 客户端的SessionUtil应该和服务端分开吧
            ClientSessionUtil.unBindSession(ctx.channel());
        } else {
            System.out.println("你当前无法退出登录！");
        }
    }
}
