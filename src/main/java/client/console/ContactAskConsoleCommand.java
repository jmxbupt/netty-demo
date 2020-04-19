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
        System.out.println("输入userId + 空格 + content，发送加好友请求: ");
        String contactId = scanner.next();
        String content = scanner.nextLine();
        channel.writeAndFlush(new ContactAskRequestPacket(contactId, content));
    }
}
