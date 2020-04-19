package protocol.request;

import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/4/19 4:36 PM
 */
public class ListContactsRequestPacket extends Packet {

    @Override
    public Byte getCommand() {
        return Command.LIST_CONTACTS_REQUEST;
    }
}
