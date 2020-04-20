package client.console;

import io.netty.channel.Channel;
import protocol.request.LogoutRequestPacket;

import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/3/10 7:48 PM
 */
public class LogoutConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

        channel.writeAndFlush(new LogoutRequestPacket());
    }
}
