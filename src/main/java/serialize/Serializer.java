package serialize;

import serialize.impl.JSONSerializer;

/**
 * @author jmx
 * @date 2020/3/10 8:57 AM
 */
public interface Serializer {

    // 单例？
    Serializer DEFAULT = new JSONSerializer();

    // 序列化算法
    Byte getSerializerAlgorithm();

    // Java对象转二进制
    byte[] serialize(Object object);

    // 二进制转Java对象
    <T> T deserialize(Class<T> tClass, byte[] bytes);
}
