package client.console;

import io.netty.channel.Channel;
import protocol.request.GroupMessageRequestPacket;

import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/3/11 11:59 AM
 */
public class SendToGroupConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

        System.out.println("输入groupId + 空格 + message，在群中发送消息: ");
        String groupId = scanner.next();
        String message = scanner.nextLine();

        GroupMessageRequestPacket groupMessageRequestPacket = new GroupMessageRequestPacket();
        groupMessageRequestPacket.setGroupId(groupId);
        groupMessageRequestPacket.setMessage(message);
        channel.writeAndFlush(groupMessageRequestPacket);
    }
}
