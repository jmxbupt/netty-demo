package server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import protocol.request.CreateGroupRequestPacket;
import protocol.response.CreateGroupResponsePacket;
import session.Session;
import util.IDUtil;
import util.SessionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jmx
 * @date 2020/3/10 8:18 PM
 */

@ChannelHandler.Sharable
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    public static final CreateGroupRequestHandler INSTANCE = new CreateGroupRequestHandler();

    private CreateGroupRequestHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket createGroupRequestPacket) {

        List<String> userIdList = createGroupRequestPacket.getUserIdList();

        List<Session> sessionList = new ArrayList<>();
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());

        // 忽略无效的userId，筛选出待加入群聊的用户的 channel 和 session
        for (String userId: userIdList) {
            Channel channel = SessionUtil.getChannel(userId);
            if (channel != null) {
                channelGroup.add(channel);
                sessionList.add(SessionUtil.getSession(channel));
            }
        }

        // 创建群聊创建结果的响应
        CreateGroupResponsePacket createGroupResponsePacket = new CreateGroupResponsePacket();

        if (channelGroup.size() > 0) {
            createGroupResponsePacket.setSuccess(true);
            createGroupResponsePacket.setGroupId(IDUtil.random());
            createGroupResponsePacket.setSession(SessionUtil.getSession(ctx.channel()));
            createGroupResponsePacket.setSessionList(sessionList);

            // 给每个客户端发送拉群通知
            channelGroup.writeAndFlush(createGroupResponsePacket);

            System.out.print("群[" + createGroupResponsePacket.getGroupId() + "]创建成功, ");
            System.out.println("群成员为：" + createGroupResponsePacket.getSessionList());

            // 保存群相关的信息
            SessionUtil.bindChannelGroup(createGroupResponsePacket.getGroupId(), channelGroup);

        } else {
            createGroupResponsePacket.setSuccess(false);
            createGroupResponsePacket.setReason("群创建失败，请检查userId列表是否有效！");
            ctx.writeAndFlush(createGroupResponsePacket);
        }





    }
}
