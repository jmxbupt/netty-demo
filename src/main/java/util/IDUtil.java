package util;

import java.util.UUID;

/**
 * @author jmx
 * @date 2020/3/10 8:29 PM
 */
public class IDUtil {
    public static String random() {
        return UUID.randomUUID().toString().split("-")[0];
    }
}
