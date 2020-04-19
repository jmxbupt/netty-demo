package protocol.response;

import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/4/19 4:37 PM
 */
public class ListContactsResponsePacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.LIST_CONTACTS_RESPONSE;
    }
}
