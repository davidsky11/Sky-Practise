通过Curator去操作zookeeper

目前Curator有2.x.x和3.x.x两个系列的版本，支持不同版本的Zookeeper。其中Curator 2.x.x兼容Zookeeper的3.4.x和3.5.x。而Curator 3.x.x只兼容Zookeeper 3.5.x，并且提供了一些诸如动态重新配置、watch删除等新特性。


Curator几个组成部分
+ Client: 是ZooKeeper客户端的一个替代品, 提供了一些底层处理和相关的工具方法
+ Framework: 用来简化ZooKeeper高级功能的使用, 并增加了一些新的功能, 比如管理到ZooKeeper集群的连接, 重试处理
+ Recipes: 实现了通用ZooKeeper的recipe, 该组件建立在Framework的基础之上
+ Utilities:各种ZooKeeper的工具类
+ Errors: 异常处理, 连接, 恢复等
+ Extensions: recipe扩展

------------------------------------
Curator主要解决了三类问题

+ 封装ZooKeeper client与ZooKeeper server之间的连接处理
+ 提供了一套Fluent风格的操作API
+ 提供ZooKeeper各种应用场景(recipe, 比如共享锁服务, 集群领导选举机制)的抽象封装


-----------------------------------------
Curator列举的ZooKeeper使用过程中的几个问题

- 初始化连接的问题: 
在client与server之间握手建立连接的过程中,如果握手失败,执行所有的同步方法(比如create,getData等)将抛出异常

- 自动恢复(failover)的问题: 当client与一台server的连接丢失,并试图去连接另外一台server时, 
client将回到初始连接模式

- session过期的问题: 在极端情况下,出现ZooKeeper 
session过期,客户端需要自己去监听该状态并重新创建ZooKeeper实例

- 对可恢复异常的处理:当在server端创建一个有序ZNode,而在将节点名返回给客户端时崩溃,此时client端抛出可恢复的异常,用户需要自己捕获这些异常并进行重试

- 使用场景的问题:Zookeeper提供了一些标准的使用场景支持,但是ZooKeeper对这些功能的使用说明文档很少,而且很容易用错.在一些极端场景下如何处理,zk并没有给出详细的文档说明.比如共享锁服务,当服务器端创建临时顺序节点成功,但是在客户端接收到节点名之前挂掉了,如果不能很好的处理这种情况,将导致死锁



-----------------------------------

Curator主要从以下几个方面降低了zk使用的复杂性

- 重试机制:提供可插拔的重试机制, 它将给捕获所有可恢复的异常配置一个重试策略,并且内部也提供了几种标准的重试策略(比如指数补偿)
- 连接状态监控: Curator初始化之后会一直的对zk连接进行监听, 一旦发现连接状态发生变化, 将作出相应的处理
- zk客户端实例管理:Curator对zk客户端到server集群连接进行管理.并在需要的情况, 重建zk实例,保证与zk集群的可靠连接
- 各种使用场景支持:Curator实现zk支持的大部分使用场景支持(甚至包括zk自身不支持的场景),这些实现都遵循了zk的最佳实践,并考虑了各种极端情况


------------------------------------
Curator声称的一些亮点

1. 日志工具 
内部采用SLF4J 来输出日志 采用驱动器(driver)机制, 允许扩展和定制日志和跟踪处理, 
提供了一个TracerDriver接口, 通过实现addTrace()和addCount()接口来集成用户自己的跟踪框架
2. 和Curator相比, 另一个ZooKeeper客户端——zkClient的不足之处 
文档几乎没有异常处理弱爆了(简单的抛出RuntimeException) 重试处理太难用了 没有提供各种使用场景的实现
3. 对ZooKeeper自带客户端(ZooKeeper类)的”抱怨” 只是一个底层实现 要用需要自己写大量的代码 很容易误用 
需要自己处理连接丢失, 重试等
