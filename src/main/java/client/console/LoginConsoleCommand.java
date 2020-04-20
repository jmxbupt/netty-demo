package client.console;

import io.netty.channel.Channel;
import protocol.request.LoginRequestPacket;

import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/3/10 7:53 PM
 */
public class LoginConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {

        System.out.print("输入用户名登录: ");
        String userName = scanner.next();
        System.out.print("输入密码: ");
        String password = scanner.next();

        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserName(userName);
        loginRequestPacket.setPassword(password);
        channel.writeAndFlush(loginRequestPacket);
    }
}
