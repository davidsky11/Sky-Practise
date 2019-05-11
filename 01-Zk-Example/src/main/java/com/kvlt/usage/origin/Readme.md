通过zookeeper原生api去操作

ZooKeeper自带客户端的主要类是ZooKeeper类,ZooKeeper类对象除了需要ZooKeeper服务端连接字符串(IP地址：端口)，还必须提供一个Watcher对象。Watcher是一个接口，当服务器节点花发生变化就会以事件的形式通知Watcher对象。所以Watcher常用来监听节点，当节点发生变化时客户端就会知道。


ZooKeeper类还有对节点进行增删改的操作方法，主要方法如下：

- create：用于创建节点，可以指定节点路径、节点数据、节点的访问权限、节点类型
- delete：删除节点，每个节点都有一个版本，删除时可指定删除的版本，类似乐观锁。设置-1，则就直接删除节点。
- exists：节点存不存在，若存在返回节点Stat信息，否则返回null
- getChildren：获取子节点
- getData/setData：获取节点数据
- getACL/setACL：获取节点访问权限列表，每个节点都可以设置访问权限，指定只有特定的客户端才能访问和操作节点。




节点类型说明： 
节点类型有4种：“PERSISTENT、PERSISTENT_SEQUENTIAL、EPHEMERAL、EPHEMERAL_SEQUENTIAL”其中“EPHEMERAL、EPHEMERAL_SEQUENTIAL”两种是客户端断开连接(Session无效时)节点会被自动删除；“PERSISTENT_SEQUENTIAL、EPHEMERAL_SEQUENTIAL”两种是节点名后缀是一个自动增长序号。

节点访问权限说明： 
节点访问权限由List确定，但是有几个便捷的静态属性可以选择： 
- Ids.CREATOR_ALL_ACL：只有创建节点的客户端才有所有权限\ 
- Ids.OPEN_ACL_UNSAFE：这是一个完全开放的权限，所有客户端都有权限 
- Ids.READ_ACL_UNSAFE：所有客户端只有读取的
