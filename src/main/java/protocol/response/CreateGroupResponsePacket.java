package protocol.response;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;
import session.Session;

import java.util.List;

/**
 * @author jmx
 * @date 2020/3/10 8:04 PM
 */
@Data
public class CreateGroupResponsePacket extends Packet {

    private boolean success;

    private String reason;

    private String userId;

    private String userName;

    private String groupId;

    private String groupName;

    @Override
    public Byte getCommand() {
        return Command.CREATE_GROUP_RESPONSE;
    }
}
