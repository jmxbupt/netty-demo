package util;

import attribute.Attributes;
import io.netty.channel.Channel;
import session.Session;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jmx
 * @date 2020/3/11 9:50 AM
 */
public class ClientSessionUtil {

    private static Map<String, Channel> userIdChannelMap = new HashMap<>();

    public static void bindSession(Session session, Channel channel) {
        userIdChannelMap.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            userIdChannelMap.remove(getSession(channel).getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    public static boolean hasLogin(Channel channel) {
        return getSession(channel) != null;
    }

    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }
}
