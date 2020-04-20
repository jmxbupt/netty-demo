package client.console;

import io.netty.channel.Channel;
import protocol.request.ListGroupsRequestPacket;

import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/4/20 8:25 PM
 */
public class ListGroupsConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

        channel.writeAndFlush(new ListGroupsRequestPacket());
    }
}
