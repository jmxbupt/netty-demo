package protocol.request;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/4/19 4:42 PM
 */
@Data
public class ContactConfirmRequestPacket extends Packet {

    String userId;

    @Override
    public Byte getCommand() {
        return Command.CONTACT_CONFIRM_REQUEST;
    }
}
