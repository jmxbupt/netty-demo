package protocol.response;

import lombok.Data;
import protocol.Packet;
import protocol.command.Command;

import java.util.List;

/**
 * @author jmx
 * @date 2020/4/19 4:37 PM
 */
@Data
public class ListContactsResponsePacket extends Packet {

    private List<String> contactAsks;

    private List<String> contacts;

    @Override
    public Byte getCommand() {
        return Command.LIST_CONTACTS_RESPONSE;
    }
}
