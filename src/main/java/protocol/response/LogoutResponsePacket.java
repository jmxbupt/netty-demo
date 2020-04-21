package protocol.response;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/3/10 7:39 PM
 */
@Data
public class LogoutResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.LOGOUT_RESPONSE;
    }
}
