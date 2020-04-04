package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.request.HeartbeatRequestPacket;
import protocol.response.HeartbeatResponsePacket;

/**
 * @author jmx
 * @date 2020/3/11 10:30 PM
 */
@ChannelHandler.Sharable
public class HeartbeatRequestHandler extends SimpleChannelInboundHandler<HeartbeatRequestPacket> {

    public static final HeartbeatRequestHandler INSTANCE = new HeartbeatRequestHandler();

    private HeartbeatRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartbeatRequestPacket heartbeatRequestPacket) {
        ctx.writeAndFlush(new HeartbeatResponsePacket());
    }
}
