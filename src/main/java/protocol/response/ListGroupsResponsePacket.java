package protocol.response;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;

import java.util.List;

/**
 * @author jmx
 * @date 2020/4/20 8:22 PM
 */
@Data
public class ListGroupsResponsePacket extends Packet {

    private List<String> groups;

    @Override
    public Byte getCommand() {
        return Command.LIST_GROUPS_RESPONSE;
    }
}
