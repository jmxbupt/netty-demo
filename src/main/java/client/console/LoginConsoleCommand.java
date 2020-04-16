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
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        System.out.print("输入用户名登录: ");
        loginRequestPacket.setUserName(scanner.next());
        System.out.print("输入密码: ");
        loginRequestPacket.setPassword(scanner.next());

        channel.writeAndFlush(loginRequestPacket);
        // 等待登录回复
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
            // 忽略
        }
    }
}
