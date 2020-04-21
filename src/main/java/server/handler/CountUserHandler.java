package server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import server.NettyServer;
import util.JDBCUtil;
import util.SessionUtil;

import java.sql.*;

/**
 * @author jmx
 * @date 2020/3/13 9:37 PM
 */
@ChannelHandler.Sharable
public class CountUserHandler extends ChannelInboundHandlerAdapter {

    public static final CountUserHandler INSTANCE = new CountUserHandler();

    private CountUserHandler() {

    }

    // channelActive和channelInactive代表TCP连接的建立与释放
    // 1.可用于统计连接数
    // 2.还可用于实现客户端ip黑白名单的过滤
    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        NettyServer.tcpCount.incrementAndGet();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

        NettyServer.tcpCount.decrementAndGet();
        // 可以处理客户端logout或者意外断线的情况

        if (SessionUtil.hasLogin(ctx.channel())) {
            // 先更新数据库中用户在线状态
            String userId = SessionUtil.getSession(ctx.channel()).getUserId();
            try (Connection conn = DriverManager.getConnection(JDBCUtil.JDBC_URL, JDBCUtil.JDBC_USER, JDBCUtil.JDBC_PASSWORD)) {
                try (PreparedStatement ps = conn.prepareStatement("UPDATE users SET online = FALSE WHERE id = ?")) {
                    ps.setObject(1, Long.valueOf(userId));
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            SessionUtil.unBindSession(ctx.channel());
            NettyServer.userCount.decrementAndGet();
        }
    }


}
