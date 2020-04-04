package protocol.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/3/11 11:53 AM
 */

@Data
@NoArgsConstructor
public class GroupMessageRequestPacket extends Packet {

    private String groupId;

    private String message;

    public GroupMessageRequestPacket(String groupId, String message) {
        this.groupId = groupId;
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return Command.GROUP_MESSAGE_REQUEST;
    }
}
