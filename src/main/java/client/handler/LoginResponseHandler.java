package client.handler;

import client.console.ConsoleCommandManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.LoginResponsePacket;

/**
 * @author jmx
 * @date 2020/3/10 1:06 PM
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {

        String userName = loginResponsePacket.getUserName();

        if (loginResponsePacket.isSuccess()) {
            String userId = loginResponsePacket.getUserId();
            System.out.println("[" + userName + "]登录成功，userId 为: " + userId);
            ConsoleCommandManager.hasLogin = true;
        } else {
            System.out.println("[" + userName + "]登录失败，原因：" + loginResponsePacket.getReason());
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 对我们的应用程序来说，channelActive 和 channelInactive 这两个方法表明的含义是 TCP 连接的建立与释放
        // 可以用来统计单机连接数
        System.out.println("客户端连接被关闭！");
    }
}
