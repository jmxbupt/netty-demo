package protocol.request;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/4/20 11:03 AM
 */
@Data
public class ContactDeleteRequestPacket extends Packet {

    String contactId;

    @Override
    public Byte getCommand() {
        return Command.CONTACT_DELETE_REQUEST;
    }
}
