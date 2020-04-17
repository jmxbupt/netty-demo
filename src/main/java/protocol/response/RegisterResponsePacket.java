package protocol.response;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/4/17 4:30 PM
 */
@Data
public class RegisterResponsePacket extends Packet {

    private boolean success;

    private String reason;

    private String userId;

    private String userName;

    @Override
    public Byte getCommand() {
        return Command.REGISTER_RESPONSE;
    }
}
