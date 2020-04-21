package client.console;

import io.netty.channel.Channel;
import protocol.request.GroupMessageRequestPacket;

import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/3/11 11:59 AM
 */
public class GroupMessageConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

        System.out.print("输入群Id：");
        String groupId = scanner.next();
        // 忽略输入流中的回车
        scanner.nextLine();
        System.out.print("输入要发送的消息：");
        String content = scanner.nextLine();

        GroupMessageRequestPacket groupMessageRequestPacket = new GroupMessageRequestPacket();
        groupMessageRequestPacket.setGroupId(groupId);
        groupMessageRequestPacket.setContent(content);
        channel.writeAndFlush(groupMessageRequestPacket);
    }
}
