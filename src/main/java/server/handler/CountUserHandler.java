package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import server.NettyServer;
import util.SessionUtil;

/**
 * @author jmx
 * @date 2020/3/13 9:37 PM
 */
@ChannelHandler.Sharable
public class CountUserHandler extends ChannelInboundHandlerAdapter {

    public static final CountUserHandler INSTANCE = new CountUserHandler();

    private CountUserHandler() {

    }

    // channelActive和channelInactive代表TCP连接的建立与释放
    // 1.可用于统计连接数
    // 2.还可用于实现客户端ip黑白名单的过滤
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        NettyServer.tcpCount.incrementAndGet();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        NettyServer.tcpCount.decrementAndGet();
        // 针对客户端没有logout意外断线的情况
        if (SessionUtil.hasLogin(ctx.channel())) {
            SessionUtil.unBindSession(ctx.channel());
            NettyServer.userCount.decrementAndGet();
        }
    }


}
