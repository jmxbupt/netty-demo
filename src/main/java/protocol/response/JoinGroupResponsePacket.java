package protocol.response;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/3/10 9:41 PM
 */

@Data
public class JoinGroupResponsePacket extends Packet {

    private String userId;

    private String userName;

    private String groupId;

    private String groupName;

    @Override
    public Byte getCommand() {
        return Command.JOIN_GROUP_RESPONSE;
    }
}
