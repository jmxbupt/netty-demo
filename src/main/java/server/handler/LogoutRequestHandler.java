package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.LogoutRequestPacket;
import protocol.response.LogoutResponsePacket;
import server.NettyServer;
import util.SessionUtil;

import java.util.List;

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

        String userId = SessionUtil.getSession(ctx.channel()).getUserId();
        List<String> groupIds = SessionUtil.getGroupIds(userId);
        for (String groupId : groupIds) {
            SessionUtil.getChannelGroup(groupId).remove(ctx.channel());
        }
        LogoutResponsePacket logoutResponsePacket = new LogoutResponsePacket();
        logoutResponsePacket.setSuccess(true);
        ctx.writeAndFlush(logoutResponsePacket);
    }
}
