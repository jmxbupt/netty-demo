package client.console;

import io.netty.channel.Channel;
import protocol.request.RegisterRequestPacket;

import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/4/17 4:08 PM
 */
public class RegisterConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

        System.out.print("输入注册用户名: ");
        String userName = scanner.next();
        System.out.print("设置密码: ");
        String password = scanner.next();

        RegisterRequestPacket registerRequestPacket = new RegisterRequestPacket();
        registerRequestPacket.setUserName(userName);
        registerRequestPacket.setPassword(password);
        channel.writeAndFlush(registerRequestPacket);
    }
}
