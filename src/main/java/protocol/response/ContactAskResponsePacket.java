package protocol.response;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/4/19 4:46 PM
 */
@Data
public class ContactAskResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.CONTACT_ASK_RESPONSE;
    }
}
