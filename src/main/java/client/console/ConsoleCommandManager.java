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

        consoleCommandMap.put("register", new RegisterConsoleCommand());
        consoleCommandMap.put("login", new LoginConsoleCommand());
        consoleCommandMap.put("logout", new LogoutConsoleCommand());

        consoleCommandMap.put("listContacts", new ListContactsConsoleCommand());
        consoleCommandMap.put("contactAsk", new ContactAskConsoleCommand());
        consoleCommandMap.put("contactConfirm", new ContactConfirmConsoleCommand());
        consoleCommandMap.put("contactDelete", new ContactDeleteConsoleCommand());

        consoleCommandMap.put("message", new MessageConsoleCommand());

        consoleCommandMap.put("createGroup", new CreateGroupConsoleCommand());
        consoleCommandMap.put("listGroupMembers", new ListGroupMembersConsoleCommand());
        consoleCommandMap.put("joinGroup", new JoinGroupConsoleCommand());
        consoleCommandMap.put("quitGroup", new QuitGroupConsoleCommand());
        consoleCommandMap.put("groupMessage", new GroupMessageConsoleCommand());


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
                "listContacts【获取好友列表】\n" +
                "contactAsk【加好友请求】\n" +
                "contactConfirm【加好友确认】\n" +
                "contactDelete【删除好友】\n" +
                "message【单聊】\n" +
                "createGroup【创建群】\n" +
                "listGroupMembers【获取群成员列表】\n" +
                "joinGroup【加入群】\n" +
                "quitGroup【退出群】\n" +
                "groupMessage【群聊】\n" +
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
