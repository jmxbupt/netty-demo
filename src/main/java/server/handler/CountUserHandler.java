package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import server.NettyServer;

/**
 * @author jmx
 * @date 2020/3/13 9:37 PM
 */
@ChannelHandler.Sharable
public class CountUserHandler extends ChannelInboundHandlerAdapter {

    public static final CountUserHandler INSTANCE = new CountUserHandler();

    private CountUserHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        NettyServer.userCount.incrementAndGet();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        NettyServer.userCount.decrementAndGet();
    }


}
