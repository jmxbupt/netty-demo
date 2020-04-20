package client.console;

import io.netty.channel.Channel;
import protocol.request.ContactConfirmRequestPacket;

import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/4/20 7:30 AM
 */
public class ContactConfirmConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

        System.out.println("输入userId，确认加好友请求：");
        String userId = scanner.next();
        ContactConfirmRequestPacket contactConfirmRequestPacket = new ContactConfirmRequestPacket();
        contactConfirmRequestPacket.setUserId(userId);
        channel.writeAndFlush(contactConfirmRequestPacket);
    }
}
