package protocol.request;

import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/3/11 10:17 PM
 */
public class HeartbeatRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.HEARTBEAT_REQUEST;
    }
}
