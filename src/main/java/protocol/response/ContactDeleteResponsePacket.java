package protocol.response;

import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/4/20 11:04 AM
 */
public class ContactDeleteResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.CONTACT_DELETE_RESPONSE;
    }
}
