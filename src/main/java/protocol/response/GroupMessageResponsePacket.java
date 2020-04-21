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

    private String userId;

    private String userName;

    private String groupId;

    private String groupName;

    private String content;

    @Override
    public Byte getCommand() {
        return Command.GROUP_MESSAGE_RESPONSE;
    }
}
