package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Assert;
import org.junit.Test;
import protocol.request.LoginRequestPacket;
import serialize.Serializer;
import serialize.impl.JSONSerializer;

/**
 * @author jmx
 * @date 2020/3/10 7:02 PM
 */
public class PacketCodecTest {
    @Test
    public void encode() {

        Serializer serializer = new JSONSerializer();
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        loginRequestPacket.setVersion(((byte) 1));
        loginRequestPacket.setUserName("zhangsan");
        loginRequestPacket.setPassword("password");

        PacketCodec packetCodec = PacketCodec.INSTANCE;
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();

        packetCodec.encode(byteBuf, loginRequestPacket);
        Packet decodedPacket = packetCodec.decode(byteBuf);

        Assert.assertArrayEquals(serializer.serialize(loginRequestPacket), serializer.serialize(decodedPacket));

    }
}

