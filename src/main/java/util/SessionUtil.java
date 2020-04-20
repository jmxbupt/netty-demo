package util;

import attribute.Attributes;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import session.Session;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jmx
 * @date 2020/3/10 5:50 PM
 */
public class SessionUtil {

    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();
    private static final Map<String, ChannelGroup> groupIdChannelGroupMap = new ConcurrentHashMap<>();
    // 用户退出时，需要处理其所在群的成员列表，所以我们在内存中额外维护一个userIdGroupIdsMap
    private static final Map<String, List<String>> userIdGroupIdsMap = new ConcurrentHashMap<>();

    public static void bindSession(Session session, Channel channel) {
        String userId = session.getUserId();
        userIdChannelMap.put(userId, channel);
        channel.attr(Attributes.SESSION).set(session);
        // 登录就绑定一个空的groupIds，后续更好处理
        userIdGroupIdsMap.put(userId, new LinkedList<>());
    }

    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            Session session = getSession(channel);
            String userId = session.getUserId();
            userIdChannelMap.remove(userId);
            userIdGroupIdsMap.remove(userId);
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

    public static void bindChannelGroup(String groupId, ChannelGroup channelGroup) {
        groupIdChannelGroupMap.put(groupId, channelGroup);
    }

    public static void unbindChannelGroup(String groupId) {
        groupIdChannelGroupMap.remove(groupId);
    }

    public static ChannelGroup getChannelGroup(String groupId) {
        return groupIdChannelGroupMap.get(groupId);
    }

    public static List<String> getGroupIds(String userId) {
        return userIdGroupIdsMap.get(userId);
    }
}
