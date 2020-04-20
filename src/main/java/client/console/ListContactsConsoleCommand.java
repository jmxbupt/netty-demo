package client.console;

import io.netty.channel.Channel;
import protocol.request.ListContactsRequestPacket;

import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/4/20 7:38 AM
 */
public class ListContactsConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

        channel.writeAndFlush(new ListContactsRequestPacket());
    }
}
