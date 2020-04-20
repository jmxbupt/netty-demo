package protocol.response;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;

import java.util.List;

/**
 * @author jmx
 * @date 2020/3/10 9:40 PM
 */

@Data
public class ListGroupMembersResponsePacket extends Packet {

    private String groupId;

    private String groupName;

    private List<String> onlineUsers;

    private List<String> offlineUsers;

    @Override
    public Byte getCommand() {
        return Command.LIST_GROUP_MEMBERS_RESPONSE;
    }
}
