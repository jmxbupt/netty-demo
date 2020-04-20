package protocol.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/3/10 11:04 AM
 */

@Data
public class MessageRequestPacket extends Packet {

    private String contactId;

    private String content;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
