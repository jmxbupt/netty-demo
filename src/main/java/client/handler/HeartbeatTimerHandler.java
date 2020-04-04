package client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import protocol.request.HeartbeatRequestPacket;

import java.util.concurrent.TimeUnit;

/**
 * @author jmx
 * @date 2020/3/11 10:23 PM
 */
public class HeartbeatTimerHandler extends ChannelInboundHandlerAdapter {

    private static final int HEARTBEAT_INTERVAL = 5;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        scheduleSendHeartBeat(ctx);

        super.channelActive(ctx);
    }

    private  void scheduleSendHeartBeat(ChannelHandlerContext ctx) {

        ctx.executor().schedule(() -> {

            if (ctx.channel().isActive()) {
                ctx.writeAndFlush(new HeartbeatRequestPacket());
                scheduleSendHeartBeat(ctx);
            }

        }, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }
}
