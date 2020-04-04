package client.console;

import io.netty.channel.Channel;
import protocol.request.QuitGroupRequestPacket;

import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/3/10 11:15 PM
 */
public class QuitGroupConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {

        System.out.print("输入groupId，退出群聊: ");
        String groupId = scanner.next();
        QuitGroupRequestPacket quitGroupRequestPacket = new QuitGroupRequestPacket();
        quitGroupRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(quitGroupRequestPacket);
    }
}
