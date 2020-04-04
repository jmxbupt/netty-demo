package protocol;

import lombok.Data;

/**
 * @author jmx
 * @date 2020/3/10 8:31 AM
 */

// 定义通信过程中Java对象的抽象类
// @Data 注解由 lombok 提供，它会自动帮我们生产 getter/setter 方法，减少大量重复代码，推荐使用。
@Data
public abstract class Packet {

    // 版本
    private Byte version = 1;

    // 指令
    public abstract Byte getCommand();
}
