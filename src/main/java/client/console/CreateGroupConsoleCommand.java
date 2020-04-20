package client.console;

import io.netty.channel.Channel;
import protocol.request.CreateGroupRequestPacket;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/3/10 7:48 PM
 */
public class CreateGroupConsoleCommand implements ConsoleCommand {

    private static final String SPLITER = ",";

    @Override
    public void exec(Scanner scanner, Channel channel) {

        // 不能输入自己的Id，且至少输入两个用户Id（否则就没有必要建群了）
        System.out.print("输入想拉入群的用户Id，用英文逗号隔开: ");
        String contactIds = scanner.next();
        System.out.print("输入群名称：");
        String groupName = scanner.next();


        CreateGroupRequestPacket createGroupRequestPacket = new CreateGroupRequestPacket();
        createGroupRequestPacket.setContactIds(Arrays.asList(contactIds.split(SPLITER)));
        createGroupRequestPacket.setGroupName(groupName);
        channel.writeAndFlush(createGroupRequestPacket);
    }
}
