package client.handler;

import client.NettyClient;
import client.console.ConsoleCommandManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author jmx
 * @date 2020/4/18 3:57 PM
 */
public class ReConnectHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

        System.out.println("客户端连接被关闭！如果没有自动重连，请输入任意字符...");
        // 这里再重新将两个变量置为初始值，针对的是服务端主动断开连接的情况
        ConsoleCommandManager.hasLogin = false;
        ConsoleCommandManager.userId = null;
        NettyClient.reConnect();
    }
}
