package protocol.response;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/4/19 4:46 PM
 */
@Data
public class ContactConfirmResponsePacket extends Packet {

    String contactId;

    @Override
    public Byte getCommand() {
        return Command.CONTACT_CONFIRM_RESPONSE;
    }
}
