package protocol.response;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;
import session.Session;

import java.util.List;

/**
 * @author jmx
 * @date 2020/3/10 9:40 PM
 */

@Data
public class ListGroupMembersResponsePacket extends Packet {

    private boolean success;

    private String reason;

    private String groupId;

    private List<Session> sessionList;


    @Override
    public Byte getCommand() {
        return Command.LIST_GROUP_MEMBERS_RESPONSE;
    }
}
