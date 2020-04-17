package client.console;

import io.netty.channel.Channel;
import util.ClientSessionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/3/10 7:45 PM
 */
public class ConsoleCommandManager implements ConsoleCommand {

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
    }


    @Override
    public void exec(Scanner scanner, Channel channel) {

        if (!ClientSessionUtil.hasLogin(channel)) {
            System.out.print("请输入register注册或login登录: ");
            String command = scanner.next();
            if (!command.equals("register") && !command.equals("login")) {
                return;
            }
            consoleCommandMap.get(command).exec(scanner, channel);
        }
        System.out.print("请输入命令：");
        String command = scanner.next();
        while (command.equals("register") || command.equals("login")) {
            System.out.println("您已登录，如需注册或者重新登录，请先输入logout退出!");
            System.out.print("请输入命令：");
            command = scanner.next();
        }
        ConsoleCommand consoleCommand = consoleCommandMap.get(command);

        if (consoleCommand != null) {
            consoleCommand.exec(scanner, channel);
        } else {
            System.out.println("无法识别[" + command + "]指令，请重新输入！");
        }
    }
}
