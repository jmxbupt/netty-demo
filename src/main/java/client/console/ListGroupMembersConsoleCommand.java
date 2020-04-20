package client.console;

import io.netty.channel.Channel;
import protocol.request.ListGroupMembersRequestPacket;

import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/3/10 11:17 PM
 */
public class ListGroupMembersConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

        System.out.print("输入群Id，获取群成员列表: ");
        String groupId = scanner.next();

        ListGroupMembersRequestPacket listGroupMembersRequestPacket = new ListGroupMembersRequestPacket();
        listGroupMembersRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(listGroupMembersRequestPacket);
    }
}
