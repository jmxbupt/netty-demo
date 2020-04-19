package protocol.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/4/19 4:40 PM
 */
@Data
@NoArgsConstructor
public class ContactAskRequestPacket extends Packet {

    String contactId;

    String content;

    public ContactAskRequestPacket(String contactId, String content) {
        this.contactId = contactId;
        this.content = content;
    }

    @Override
    public Byte getCommand() {
        return Command.CONTACT_ASK_REQUEST;
    }
}
