## 基于Netty实现的IM即时通讯系统

> 本项目在闪电侠提供教程的基础上进行了修改，参考：
> 
> 掘金小册地址：[Netty入门与实战：仿写微信IM即时通讯系统](https://juejin.im/book/5b4bc28bf265da0f60130116)
> 
> github地址：https://github.com/lightningMan/flash-netty
>
> 我感觉这个教程最好的地方在于把整体框架定得很清晰，这样让功能的扩充变得简单
>
> - 实现的功能包括心跳检测、登录、鉴权、单聊、建群、获取群成员列表、群聊、加群、退群、退出登录
> - 还进行了一系列优化，包括使用SimpleChannelInboundHandler避免大量的if...else...、使用单例、合并编解码器、压缩事件传播路径等
> 
> 之前跟着教程敲了一遍代码，对应firstCommit（和教程的代码有一些小出入）
> 
> 不过教程的重心是在netty的基本使用上，还有很多可以改进和发挥的空间

### v1（2020.04.21）

主要的改进是使用了数据库，包括以下几张表

- users【用户信息表】
- contactAsks【联系请求表】
- contacts【联系人表】
- messages【消息表】
- groups【群信息表】
- group2user【群成员表】
- groupMessages【群消息表】

同时新增了一些功能，目前的功能如下

- register【注册】
- login【登录】
- listContacts【获取好友列表】
- contactAsk【加好友请求】
- contactConfirm【加好友确认】
- contactDelete【删除好友】
- message【单聊】
- listGroups【获取群列表】
- createGroup【创建群】
- listGroupMembers【获取群成员列表】
- joinGroup【加入群】
- quitGroup【退出群】
- groupMessage【群聊】
- logout【退出登录】

关于在线离线
- 只能给在线好友发消息
- 建群的时候允许拉离线好友
- 离线用户收不到群消息

修复了几个主要的bug

1. 在没有使用数据库持久存储之前，用户退出之后，应该更新相关群的成员列表，否则在获取群成员列表时会出现null（通过在内存中维护了一个userId到groupIds的ConcurrentHashMap解决，这样可以避免遍历所有的群，然后看用户是否在其中。当然，使用数据库之后，又重新修改了逻辑，用户退出登录之后是不会退群的）
2. 用户鉴权完成之后退出登录，此时并没有断开TCP连接，但是pipeline中已经没有AuthHandler了，这可能造成一些隐患（作者在评论中给出的方案是退出登录之后就断开TCP连接，然后自动重连，这里进行了实现）
3. 实现了服务端定时统计单机连接数和在线用户数的逻辑——特别需要注意的是，要能处理客户端logout或者意外断线的情况
4. 如果服务端崩溃，之前还在线的用户在users表中的online字段为1，这样listContacts, listGroupMembers等区分在线用户和离线用户的命令就会返回错误结果（通过在启动服务端的时候将users表中所有记录的online字段置为0解决）
5. 因为还没有实际的UI，所以都是在控制台输入来模拟命令，中途为了方便，我没有新创建线程，而是在NettyClient的主线程中写了一个while循环，这让主线程一直卡在while循环中，无法处理回调（解决方案，嗯，还是新创建一个线程接收控制台的输入，组装成requestPacket之后发送给服务端）
6. Session的概念应该只存在于服务端（服务端在写响应的时候不要用Session，而是将数据直接拼接成String）


待改进

1. 使用线程池执行数据库相关操作（因为一个NIO线程通常管理着几千到几万个连接，其中只要有一个channel的一个handler中的channelRead0()方法阻塞了NIO线程，最终都会拖慢绑定在该NIO线程上的其他所有的channel）
2. 好友模块：添加备注和分组功能
3. 群模块：群设置人数上限；修改群名；群建立完成之后拉人进群
4. 可以给离线用户发消息
