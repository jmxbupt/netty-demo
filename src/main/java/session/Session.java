package session;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jmx
 * @date 2020/3/10 5:52 PM
 */

@Data
@NoArgsConstructor
public class Session {

    // 用户唯一标志，登录之后由服务器随机生成
    private String userId;
    private String userName;

    // 实际生产环境中，Session中的字段可能较多，比如头像，url，年龄，性别等等。
    public Session(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return userId + ":" + userName;
    }
}
