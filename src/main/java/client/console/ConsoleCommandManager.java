package client.console;

import io.netty.channel.Channel;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/3/10 7:45 PM
 */
public class ConsoleCommandManager implements ConsoleCommand {

    public static boolean hasLogin = false;
    public static String userId;

    private Map<String, ConsoleCommand> consoleCommandMap;

    public ConsoleCommandManager() {
        consoleCommandMap = new HashMap<>();
        consoleCommandMap.put("login", new LoginConsoleCommand());
        consoleCommandMap.put("sendToUser", new SendToUserConsoleCommand());
        consoleCommandMap.put("logout", new LogoutConsoleCommand());
        consoleCommandMap.put("createGroup", new CreateGroupConsoleCommand());
        consoleCommandMap.put("listGroupMembers", new ListGroupMembersConsoleCommand());
        consoleCommandMap.put("joinGroup", new JoinGroupConsoleCommand());
        consoleCommandMap.put("quitGroup", new QuitGroupConsoleCommand());
        consoleCommandMap.put("sendToGroup", new SendToGroupConsoleCommand());
        consoleCommandMap.put("register", new RegisterConsoleCommand());
        consoleCommandMap.put("contactAsk", new ContactAskConsoleCommand());
    }


    @Override
    public void exec(Scanner scanner, Channel channel) {

        while (!hasLogin) {
            System.out.print("请输入：\n" +
                    "----------\n" +
                    "register【注册】\n" +
                    "login【登录】\n" +
                    "----------\n");
            String command = scanner.next();
            if (command.equals("register") || command.equals("login")) {
                consoleCommandMap.get(command).exec(scanner, channel);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                    // 忽略
                }
            }

        }

        System.out.print("请输入：\n" +
                "----------\n" +
                "contactAsk【加好友】\n" +
                "sendToUser【单聊】\n" +
                "createGroup【创建群】\n" +
                "listGroupMembers【获取群成员列表】\n" +
                "joinGroup【加入群】\n" +
                "quitGroup【退出群】\n" +
                "sendToGroup【群聊】\n" +
                "logout【退出登录】\n" +
                "----------\n");
        String command = scanner.next();
        if (command.equals("register") || command.equals("login")) {
            System.out.println("您已登录，如需注册或者重新登录，请先输入logout退出登录!");
            return;
        }
        ConsoleCommand consoleCommand = consoleCommandMap.get(command);

        if (consoleCommand != null) {
            consoleCommand.exec(scanner, channel);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
                // 忽略
            }
        } else {
            System.out.println("无法识别[" + command + "]指令，请重新输入！");
        }
    }
}
