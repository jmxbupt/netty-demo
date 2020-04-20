package client.console;

import io.netty.channel.Channel;
import protocol.request.ContactAskRequestPacket;

import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/4/19 10:29 PM
 */
public class ContactAskConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

        System.out.print("输入用户Id，请求加好友：");
        String contactId = scanner.next();
        // 忽略输入流中的回车
        scanner.nextLine();
        System.out.print("输入请求消息：");
        String content = scanner.nextLine();

        ContactAskRequestPacket contactAskRequestPacket = new ContactAskRequestPacket();
        contactAskRequestPacket.setContactId(contactId);
        contactAskRequestPacket.setContent(content);
        channel.writeAndFlush(contactAskRequestPacket);
    }
}
