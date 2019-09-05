# P6 编码题（testing_01)

实现一个轻量级容器和火车票交易系统（含轻量级db）

要求：
1. 不引入第三方包. 全部使用JDK 1.6以上标准api。
2. container 是一个轻量级spring 容器，从beans.properties 中读取class列表。需要
  实现注解依赖aop 拦截器实现。
2. 不能删除已经提供的java代码，服务配置beans.properties 可以任意修改，但不能覆盖
  SimpleDB实现。
3. 建议0.5 ~ 3小时内编码，根据个人偏好选择部分功能实现。如果有兴趣做完整最好。
4. 需要记录，改进的思路、比较突出的亮点、后续优化规划、目前存在的问题，修改的代码注
  意细节的优化。
5. 代码合理使用领域模型设计、设计模式，考虑集群环境下优化。 (!!重要，合理模块设计比
  实现功能更重要!!)

场景：
1. 初始100万张票
2. 10万用户购买，20个售卖窗口（并发 + 集群），每个用户购买量随机。
3. 每个用户只能购买一次，退票后可以再次购买，每人最多退票1次。
4. aop拦截器实现权限检测，用户名为：no_login 不能购买，huang_niu 会随机性退票
5. 注意db后需要commit，db会间歇性不可用，应用层需要做好容错。
6. 入口：com.alipay.train.main.Starter

进阶：
1. 考虑大数据量场景，不是100万张票，是10亿票， 3亿人民、10万窗口购买。
2. 引入netty实现分布式容器，分布式事务支持等。因为时间考虑不建议实现，在提交基础版
  后有兴趣挑战更高岗位可以自由发挥。
