package client.console;

import io.netty.channel.Channel;
import protocol.request.ContactDeleteRequestPacket;

import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/4/20 11:07 AM
 */
public class ContactDeleteConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

        System.out.print("输入用户Id，删除好友：");
        String contactId = scanner.next();

        ContactDeleteRequestPacket contactDeleteRequestPacket = new ContactDeleteRequestPacket();
        contactDeleteRequestPacket.setContactId(contactId);
        channel.writeAndFlush(contactDeleteRequestPacket);
    }
}
