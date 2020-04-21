package client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.ListContactsResponsePacket;

/**
 * @author jmx
 * @date 2020/4/20 7:43 AM
 */
public class ListContactsResponseHandler extends SimpleChannelInboundHandler<ListContactsResponsePacket> {

    private static final String DELIMETER = "----------";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListContactsResponsePacket listContactsResponsePacket) {

        System.out.println("未处理的加好友请求");
        System.out.println(DELIMETER);
        for (String str : listContactsResponsePacket.getContactAsks()) {
            System.out.println(str);
        }
        System.out.println(DELIMETER);

        System.out.println("在线好友");
        System.out.println(DELIMETER);
        for (String str : listContactsResponsePacket.getOnlineContacts()) {
            System.out.println(str);
        }
        System.out.println(DELIMETER);

        System.out.println("离线好友");
        System.out.println(DELIMETER);
        for (String str : listContactsResponsePacket.getOfflineContacts()) {
            System.out.println(str);
        }
        System.out.println(DELIMETER);

    }
}
