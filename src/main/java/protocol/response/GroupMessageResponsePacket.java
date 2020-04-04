package protocol.response;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;
import session.Session;

/**
 * @author jmx
 * @date 2020/3/11 11:55 AM
 */

@Data
public class GroupMessageResponsePacket extends Packet {

    private boolean success;

    private String reason;

    private String fromGroupId;

    private Session fromUser;

    private String message;

    @Override
    public Byte getCommand() {
        return Command.GROUP_MESSAGE_RESPONSE;
    }
}
