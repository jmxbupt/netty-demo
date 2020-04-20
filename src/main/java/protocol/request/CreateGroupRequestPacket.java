package protocol.request;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;

import java.util.List;

/**
 * @author jmx
 * @date 2020/3/10 8:03 PM
 */
@Data
public class CreateGroupRequestPacket extends Packet {

    private List<String> contactIds;

    private String groupName;

    @Override
    public Byte getCommand() {
        return Command.CREATE_GROUP_REQUEST;
    }
}
