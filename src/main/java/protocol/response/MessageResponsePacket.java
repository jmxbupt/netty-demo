package protocol.response;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/3/10 11:05 AM
 */

@Data
public class MessageResponsePacket extends Packet {

    private String userId;

    private String userName;

    private String content;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_RESPONSE;
    }
}
