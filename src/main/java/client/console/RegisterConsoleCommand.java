package client.console;

import io.netty.channel.Channel;
import protocol.request.LoginRequestPacket;
import protocol.request.RegisterRequestPacket;

import java.util.Scanner;

/**
 * @author jmx
 * @date 2020/4/17 4:08 PM
 */
public class RegisterConsoleCommand implements ConsoleCommand {

    @Override
    public void exec(Scanner scanner, Channel channel) {
        RegisterRequestPacket registerRequestPacket = new RegisterRequestPacket();

        System.out.print("输入注册用户名: ");
        registerRequestPacket.setUserName(scanner.next());
        System.out.print("设置密码: ");
        registerRequestPacket.setPassword(scanner.next());

        channel.writeAndFlush(registerRequestPacket);
        // 等待登录回复
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
            // 忽略
        }
    }
}
