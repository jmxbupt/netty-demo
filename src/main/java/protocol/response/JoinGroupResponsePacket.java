package protocol.response;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;
import session.Session;

/**
 * @author jmx
 * @date 2020/3/10 9:41 PM
 */

@Data
public class JoinGroupResponsePacket extends Packet {

    private boolean success;

    private String reason;

    private String groupId;

    private Session session;

    @Override
    public Byte getCommand() {
        return Command.JOIN_GROUP_RESPONSE;
    }
}
