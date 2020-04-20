package protocol.request;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/4/20 8:21 PM
 */
public class ListGroupsRequestPacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.LIST_GROUPS_REQUEST;
    }
}
