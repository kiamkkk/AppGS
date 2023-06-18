# 技术栈：
springboot，spring Security, MySQL, Mybatis, jwt, rabbitMQ(处理聊天信息)，Redis（管理token），websocket， minio
# 接口文档：
> https://apifox.com/apidoc/shared-d55d4e83-9729-4bbf-9d1e-4784fdbec82c
# 项目亮点:
## 技术亮点
使用minio存储部分图片
使用rabbitMQ作为消息处理中间件
## 设计亮点
商品带有不同标签，发布者可以用不同标签来表示商品特点
## 封装亮点
- TokenUtil：token的签发、检验等操作封装在TokenUtil中，方便重复调用
- MinioUtil：将从minio中存入、删除、获取图片的操作封装在MinioUtil中，支持批量操作
# 未解决的问题
- 新订单插入有问题
  - 问题：商品因交易异常或交易取消后,不能对同一个商品再一次建立订单,因为goodId是唯一索引.
  - 疑问：暂无
  - 目前解决方法：取消goodId的唯一索引; 插入新bill时检查有无state为在交易的goodId.
- 对接支付宝时，没有完全处理支付宝返回的异常
  - 问题：支付宝接口可能返回的异常太多了，没时间针对每个异常进行处理
  - 疑问：实际开发中是怎么处理这些异常的？
  - 目前解决方案：支付宝返回异常时，对用户返回500响应
- 售后如何向管理员通过聊天反馈还没想清楚，现在设计的有点问题还在改
# 当前进度
前后端接口联调中。。。
# 加分项
使用websocket与stomp协议，保存聊天记录并支持文字交流，支持图片交流，支持消息提醒
利用jwt区分用户身份，确认用户权限
支持对接真实支付充值功能
项目部署已经到云服务器上，部分组件使用docker部署
