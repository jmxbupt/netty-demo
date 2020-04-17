package protocol.request;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/4/17 4:28 PM
 */
@Data
public class RegisterRequestPacket extends Packet {

    private String userName;

    private String password;

    @Override
    public Byte getCommand() {
        return Command.REGISTER_REQUEST;
    }
}
