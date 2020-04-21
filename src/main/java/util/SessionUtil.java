package util;

import attribute.Attributes;
import io.netty.channel.Channel;
import session.Session;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jmx
 * @date 2020/3/10 5:50 PM
 */
public class SessionUtil {

    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {
        String userId = session.getUserId();
        userIdChannelMap.put(userId, channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            Session session = getSession(channel);
            String userId = session.getUserId();
            userIdChannelMap.remove(userId);
            channel.attr(Attributes.SESSION).set(null);
            System.out.println(new Date() + "：" + session + "退出登录！");
        }
    }

    public static boolean hasLogin(Channel channel) {
        return getSession(channel) != null;
    }

    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(String userId) {
        return userIdChannelMap.get(userId);
    }
}
