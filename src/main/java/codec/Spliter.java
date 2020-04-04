package codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import protocol.PacketCodec;

/**
 * @author jmx
 * @date 2020/3/10 2:33 PM
 */

// 应用层面使用了netty，按ByteBuf发送数据，但底层仍然是TCP，按字节发送，收到之后需要按协议拆开
// 基于长度域的拆包器是netty自带的最通用的拆包器
// 每次读到一定的数据，都会累加到一个容器里面，然后判断是否能够拆出来一个完整的数据包，如果够的话就拆了之后，往下进行传递
public class Spliter extends LengthFieldBasedFrameDecoder {

    private static final int LENGTH_FILED_OFFSET = 7;
    private static final int LENGTH_FILED_LENGTH = 4;

    public Spliter() {
        super(Integer.MAX_VALUE, LENGTH_FILED_OFFSET, LENGTH_FILED_LENGTH);
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 屏蔽非本协议的客户端
        // in 每次传递进来的时候，均为一个数据包的开头
        // 这里不能用readInt，否则会改变读指针，影响后续解码
        if (in.getInt(0) != PacketCodec.MAGIC_NUMBER) {
            ctx.channel().close();
            return null;
        }
        return super.decode(ctx, in);
    }
}
