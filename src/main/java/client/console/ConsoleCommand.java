package client.console;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/3/10 7:44 PM
 */
public interface ConsoleCommand {
    void exec(Scanner scanner, Channel channel);
}
