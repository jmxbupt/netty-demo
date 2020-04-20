package client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protocol.response.ListContactsResponsePacket;

import java.util.StringJoiner;

/**
 * @author jmx
 * @date 2020/4/20 7:43 AM
 */
public class ListContactsResponseHandler extends SimpleChannelInboundHandler<ListContactsResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListContactsResponsePacket listContactsResponsePacket) {

        System.out.println("未处理的加好友请求");
        String delimeter = "----------";
        StringJoiner sj = new StringJoiner("\n", delimeter + "\n", "\n" + delimeter);
        for (String str: listContactsResponsePacket.getContactAsks()) {
            sj.add(str);
        }
        System.out.println(sj.toString());

        System.out.println("在线好友");
        sj = new StringJoiner("\n", delimeter + "\n", "\n" + delimeter);
        for (String str: listContactsResponsePacket.getOnlineContacts()) {
            sj.add(str);
        }
        System.out.println(sj.toString());

        System.out.println("离线好友");
        sj = new StringJoiner("\n", delimeter + "\n", "\n" + delimeter);
        for (String str: listContactsResponsePacket.getOfflineContacts()) {
            sj.add(str);
        }
        System.out.println(sj.toString());

    }
}
