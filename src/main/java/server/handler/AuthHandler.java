package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import util.SessionUtil;

import java.util.Date;

/**
 * @author jmx
 * @date 2020/3/10 4:09 PM
 */

// 使用channelHandler的热插拔实现用户身份校验
// 这里继承ChannerlInboundHandlerAdapter，表示可以处理所有类型的数据
@ChannelHandler.Sharable
public class AuthHandler extends ChannelInboundHandlerAdapter {

    public static final AuthHandler INSTANCE = new AuthHandler();

    private AuthHandler() {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (!SessionUtil.hasLogin(ctx.channel())) {
            // 没有登录就直接关闭这条连接，不过实际生产环境不会这么粗暴
            ctx.channel().close();
        } else {
            // 已经登录，就从这条连接的逻辑链中删除这个handler
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {

        if (SessionUtil.hasLogin(ctx.channel())) {
            System.out.println(new Date() + "：[" + SessionUtil.getSession(ctx.channel()).getUserName() + "]登录验证完毕，无需再次验证");
        } else {
            System.out.println("无登录验证，强制关闭连接!");
        }
    }

}
