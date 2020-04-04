package protocol.response;

import protocol.Packet;
import protocol.command.Command;

/**
 * @author jmx
 * @date 2020/3/11 10:18 PM
 */
public class HeartbeatResponsePacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.HEARTBEAT_RESPONSE;
    }
}
