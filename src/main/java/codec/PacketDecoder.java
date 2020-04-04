package codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import protocol.PacketCodec;

import java.util.List;

/**
 * @author jmx
 * @date 2020/3/10 12:36 PM
 */
public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        // 继承ByteToMessageDecoder，回调channelRead方法之后，会调用我们实现的decode方法
        // 通过向 out 添加解码后的结果对象，就可以自动实现结果往下一个 handler 的传递
        // Netty 会自动进行 ByteBuf 内存的释放
        out.add(PacketCodec.INSTANCE.decode(in));
    }
}
