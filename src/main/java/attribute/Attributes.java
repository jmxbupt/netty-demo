package attribute;

import io.netty.util.AttributeKey;
import session.Session;

/**
 * @author jmx
 * @date 2020/3/10 11:10 AM
 */
public interface Attributes {
    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}
