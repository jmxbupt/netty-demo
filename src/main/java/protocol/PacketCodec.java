package protocol;

import io.netty.buffer.ByteBuf;
import protocol.command.Command;
import protocol.request.*;
import protocol.response.*;
import serialize.Serializer;
import serialize.impl.JSONSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jmx
 * @date 2020/3/10 9:07 AM
 */
public class PacketCodec {

    public static final int MAGIC_NUMBER = 0x12345678;
    // 单例模式？
    public static final PacketCodec INSTANCE = new PacketCodec();

    // 这里可以用final?
    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private static final Map<Byte, Serializer> serializerMap;

    // 这里的static是什么含义？
    static {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);
        packetTypeMap.put(Command.LOGOUT_REQUEST, LogoutRequestPacket.class);
        packetTypeMap.put(Command.LOGOUT_RESPONSE, LogoutResponsePacket.class);
        packetTypeMap.put(Command.CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        packetTypeMap.put(Command.CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);
        packetTypeMap.put(Command.LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestPacket.class);
        packetTypeMap.put(Command.LIST_GROUP_MEMBERS_RESPONSE, ListGroupMembersResponsePacket.class);
        packetTypeMap.put(Command.JOIN_GROUP_REQUEST, JoinGroupRequestPacket.class);
        packetTypeMap.put(Command.JOIN_GROUP_RESPONSE, JoinGroupResponsePacket.class);
        packetTypeMap.put(Command.QUIT_GROUP_REQUEST, QuitGroupRequestPacket.class);
        packetTypeMap.put(Command.QUIT_GROUP_RESPONSE, QuitGroupResponsePacket.class);
        packetTypeMap.put(Command.GROUP_MESSAGE_REQUEST, GroupMessageRequestPacket.class);
        packetTypeMap.put(Command.GROUP_MESSAGE_RESPONSE, GroupMessageResponsePacket.class);
        packetTypeMap.put(Command.HEARTBEAT_REQUEST, HeartbeatRequestPacket.class);
        packetTypeMap.put(Command.HEARTBEAT_RESPONSE, HeartbeatResponsePacket.class);
        packetTypeMap.put(Command.REGISTER_REQUEST, RegisterRequestPacket.class);
        packetTypeMap.put(Command.REGISTER_RESPONSE, RegisterResponsePacket.class);
        packetTypeMap.put(Command.CONTACT_ASK_REQUEST, ContactAskRequestPacket.class);
        packetTypeMap.put(Command.CONTACT_ASK_RESPONSE, ContactAskResponsePacket.class);
        packetTypeMap.put(Command.CONTACT_CONFIRM_REQUEST, ContactConfirmRequestPacket.class);
        packetTypeMap.put(Command.CONTACT_CONFIRM_RESPONSE, ContactConfirmResponsePacket.class);
        packetTypeMap.put(Command.LIST_CONTACTS_REQUEST, ListContactsRequestPacket.class);
        packetTypeMap.put(Command.LIST_CONTACTS_RESPONSE, ListContactsResponsePacket.class);
        packetTypeMap.put(Command.CONTACT_DELETE_REQUEST, ContactDeleteRequestPacket.class);
        packetTypeMap.put(Command.CONTACT_DELETE_RESPONSE, ContactDeleteResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }


    public void encode(ByteBuf byteBuf, Packet packet) {
        // 序列化是将对象序列化为网络可传输的二进制数据
        // 编码是为了程序能理解二进制流数据的含义，以便恢复出对象

        // 序列化Java对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }


    public Packet decode(ByteBuf byteBuf) {

        // 跳过魔数
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法
        byte serializerAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializerAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }

    private Serializer getSerializer(byte serializerAlgorithm) {
        return serializerMap.get(serializerAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }

}
