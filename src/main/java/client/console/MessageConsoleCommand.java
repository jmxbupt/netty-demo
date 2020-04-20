package client.console;

import io.netty.channel.Channel;
import protocol.request.MessageRequestPacket;

import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/3/10 7:48 PM
 */
public class MessageConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

        System.out.print("输入用户ID：");
        String contactId = scanner.next();
        // 忽略输入流中的回车
        scanner.nextLine();
        System.out.print("输入要发送的消息：");
        String content = scanner.nextLine();

        MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
        messageRequestPacket.setContactId(contactId);
        messageRequestPacket.setContent(content);
        channel.writeAndFlush(messageRequestPacket);
    }
}
