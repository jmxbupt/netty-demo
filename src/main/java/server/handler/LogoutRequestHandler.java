package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.LogoutRequestPacket;
import protocol.response.LogoutResponsePacket;

/**
 * @author jmx
 * @date 2020/3/10 8:13 PM
 */

@ChannelHandler.Sharable
public class LogoutRequestHandler extends SimpleChannelInboundHandler<LogoutRequestPacket> {

    public static final LogoutRequestHandler INSTANCE = new LogoutRequestHandler();

    private LogoutRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutRequestPacket logoutRequestPacket) {

        LogoutResponsePacket logoutResponsePacket = new LogoutResponsePacket();
        ctx.writeAndFlush(logoutResponsePacket);
    }
}
