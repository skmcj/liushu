# 流书网-数据库设计文档

## 介绍

- 数据库名称 — `liushu`

- 数据库类型 — `MySQL`

- 数据库版本 — `8.0.26`

- 默认端口 — `3306`

- 版本：`v1.0.0`

- 数据库文件：`./liushu.sql`，仅包含表结构及一些必要数据，如需包含测试数据，可[点击此处](https://pan.baidu.com/s/1fTOWo2SAUvix_zRleAdoDQ?pwd=ls9d)下载

  


```mysql
-- 创建数据库
CREATE DATABASE `liushu` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

SET FOREIGN_KEY_CHECKS=0; 
```

## 用户表

- `user` — 用户表

  - | 字段名        | 类型       | 长度 | 非空 | 键   | 备注                                  |
    | ------------- | ---------- | ---- | ---- | ---- | ------------------------------------- |
    | `id`          | `bigint`   |      | ✔    | 🔑    | 主键，用户`id`、`im_id`(通信服务`id`) |
    | `username`    | `varchar`  | 32   | ✔    |      | 用户名，字符数字组合，6位及以上       |
    | `password`    | `varchar`  | 216  | ✔    |      | 密码，使用`md5`加密                   |
    | `email`       | `varchar`  | 255  | ✔    |      | 邮箱                                  |
    | `create_time` | `datetime` |      |      |      | 创建时间                              |
    | `update_time` | `datetime` |      |      |      | 更新时间                              |
    | `status`      | `int`      |      |      |      | 状态，0-禁用；1-正常，默认为1         |
    
    
  
- `user_info` — 用户信息表

  - | 字段名 | 类型 | 长度[小数位] | 非空 | 键   | 备注 |
    | ------ | ---- | ---- | ---- | ---- | ---- |
    | `id` | `bigint` |  | ✔ | 🔑 | 主键，用户详情记录`id` |
    | `user_id` | `bigint` |  | ✔ |  | 对应用户`id` |
    | `nickname`  | `varchar`  | 32   |      |      | 昵称                          |
    | `sex`       | `varchar`  | 2    |      |      | 性别，0-未知；1-男；2-女      |
    | `avatar`    | `varchar`  | 500  |      |      | 头像                          |
    | `birthday`  | `datetime` |      |      |      | 生日                          |
    | `signature` | `varchar`  | 200  |      |      | 个性签名                      |
    | `money` | `decimal` | 10[2] | | | 账户零钱余额 |
    | `pay_pass` | `varchar` | 10 | | | 支付密码 |
    | `create_time` | `datetime` |  | | | 创建时间 |
    | `update_time` | `datetime` |  | | | 更新时间 |
  
  - 其中，`pay_pass`使用`sha256`加密后存储，只是为了简单实现，如有需求，可使用其它更安全的加密算法，并增加密码试错限制。
  
- `user_money_record` — 用户零钱变动记录表（暂未用到）

  - | 字段名 | 类型 | 长度[小数位] | 非空 | 键   | 备注 |
    | ------ | ---- | ---- | ---- | ---- | ---- |
    | `id` | `bigint` |  | ✔ | 🔑 | 主键，余额变动记录`id` |
    | `user_id` | `bigint` |  | ✔ |  | 对应用户`id` |
    | `type`  | `varchar`  | 64 |      |      | 类型标签，如充值                  |
    | `change`  | `int` |     |      |      | 变动类型，0-增加；1-减少 |
    | `amount` | `decimal` | 10 | 2 |      | 金额                        |
    | `create_time` | `datetime` |      |      |      | 创建时间                      |
    | `update_time` | `datetime` |   |      |      | 更新时间                  |

- 如有优惠卷表等其它与用户绑定的表(根据需求扩展)，可设计后通过(`user_id`)用户表`id`关联用户表即可

## 商家表

- `bookstore` — 书店表

  - | 字段名           | 类型      | 长度[小数位] | 非空 | 键   | 备注                                 |
    | ---------------- | --------- | ------------ | ---- | ---- | ------------------------------------ |
    | `id`             | `bigint`  |              | ✔    | 🔑    | 主键，`id`、`im_id`             |
    | `email`          | `varchar` | 216        | ✔    |      | 邮箱                                 |
    | `password` | `varchar` | 216 |  | | 店长(店铺所有者)密码 |
    | `store_name`     | `varchar` | 64         |     |      | 店铺名称                             |
    | `cover`          | `varchar` | 216        |     |      | 门脸图片                             |
    | `score`          | `decimal` | 2[1] |     |      | 评分                                 |
    | `address`        | `varchar` | 216        |     |      | 店铺地址                             |
    | `business_hours` | `varchar` | 32           |     |      | 营业时间                             |
    | `distribution`   | `int`     |              |      |      | 配送服务，0-由商家配送               |
    | `delivery_fee`   | `decimal` | 10[2] |      |      | 配送费                               |
    | `borrow_day` | `int` | | | | 首次借阅天数[^2.1] |
    | `renew_day` | `int` | | | | 可续借天数[^2.2] |
    | `store_service`  | `varchar` | 216          |      |      | 商家服务                             |
    | `label`          | `varchar` | 216        |      |      | 标签                                 |
    | `notice`         | `varchar` | 216          |      |      | 公告                                 |
    | `income`           | `decimal` | 10[2]        |      |      | 收入               |
    | `status`         | `int`     |              |      |      | 店铺状态，0-休息；1-营业             |
    | `audit_status`   | `int`     |              |      |      | 审核状态，0-未提交；1-审核中；2-通过；3-不通过 |
    | `mba` | `int` | | | | 商家月借阅量 |
    | `prefix` | `varchar` | 128 | | | 商家前缀，用于区分商家内员工的用户名 |
    | `create_time` | `datetime` |              |      |      | 创造时间                       |
    | `update_time` | `datetime` |              |      |      | 更新时间                       |
  
- `bookstore_detail` — 书店详情表

  - | 字段名             | 类型      | 长度[小数位] | 非空 | 键   | 备注               |
    | ------------------ | --------- | ------------ | ---- | ---- | ------------------ |
    | `id`               | `bigint`  |              | ✔    | 🔑    | 主键，书店详情`id` |
    | `store_id`         | `bigint`  |              | ✔    |      | 书店`id`           |
    | `owner_name`       | `varchar` | 24           | ✔    |      | 法人姓名           |
    | `id_number`        | `varchar` | 24           | ✔    |      | 法人身份证号       |
    | `phone`            | `varchar` | 24         | ✔    |      | 法人手机号         |
    | `business_license` | `varchar` | 255         |     |      | 营业执照           |
    | `license_img`      | `varchar` | 255         |     |      | 出版物经营许可证   |
    | `env_imgs`         | `varchar` | 255         |     |      | 店内环境           |
    | `create_time` | `datetime` |              |      |      | 创造时间                       |
    | `update_time` | `datetime` |              |      |      | 更新时间                       |



- `employee` — 书店员工表

  - | 字段名        | 类型       | 长度[小数位] | 非空 | 键   | 备注                           |
    | ------------- | ---------- | ------------ | ---- | ---- | ------------------------------ |
    | `id`          | `bigint`   |              | ✔    | 🔑    | 主键，员工`id`                 |
    | `store_id`    | `bigint`   |              | ✔    |      | 书店`id`                       |
    | `name`        | `varchar`  | 24           |      |      | 员工名称                       |
    | `id_number`   | `varchar`  | 24           |      |      | 身份证号                       |
    | `phone`       | `varchar`  | 24           |      |      | 手机号                         |
    | `email`       | `varchar`  | 216          |      |      | 邮箱号                         |
    | `username`    | `varchar`  | 32           |      |      | 用户名                         |
    | `password`    | `varchar`  | 216          |      |      | 密码                           |
    | `sex`         | `int`      |              |      |      | 性别，0-未知；1-男；2-女       |
    | `competence`  | `int`      |              |      |      | 权限，0-店长；1-管理员；2-员工 |
    | `status`      | `int`      |              |      |      | 状态，0-禁用；1-正常           |
    | `is_deleted`  | `int`      |              |      |      | 逻辑删除，1-删除；0-正常       |
    | `create_time` | `datetime` |              |      |      | 创造时间                       |
    | `update_time` | `datetime` |              |      |      | 更新时间                       |
    | `create_user` | `bigint`   |              |      |      | 创造者`id`                     |
    | `update_user` | `bigint`   |              |      |      | 更新者`id`                     |



- `goods_cate` — 店内分类表

  - | 字段名        | 类型 | 长度[小数位] | 非空 | 键   | 备注 |
    | ------------- | ---- | ------------ | ---- | ---- | ---- |
    | `id`          | `bigint` |              | ✔ | 🔑 | 主键，分类`id` |
    | `store_id`    | `bigint` |              | ✔ |      | 书店`id` |
    | `name`        | `varchar` | 64 | ✔ |      | 分类名称 |
    | `description` | `varchar` | 216 |  |      | 分类描述 |
    | `sort`        | `int` |              |  |      | 排序依据 |
    | `create_time` | `datetime` |              |      |      | 创造时间                       |
    | `update_time` | `datetime` |              |      |      | 更新时间                       |
    | `create_user` | `bigint` |              |      |      | 创造者                         |
    | `update_user` | `bigint` |              |      |      | 更新者                         |



## 图书表

- `book` — 图书表

  - | 字段名          | 类型       | 长度[小数位] | 非空 | 键   | 备注                     |
    | --------------- | ---------- | ------------ | ---- | ---- | ------------------------ |
    | `id`            | `bigint`   |              | ✔    | 🔑    | 主键，图书`id`           |
    | `store_id`      | `bigint`   |              | ✔    |      | 所属书店`id`             |
    | `cover`         | `varchar`  | 216          | ✔    |      | 封面                     |
    | `name`          | `varchar`  | 128          | ✔    |      | 图书名称                 |
    | `author`        | `varchar`  | 128          | ✔    |      | 作者                     |
    | `book_cate_id`  | `bigint`   |              | ✔    |      | 图书所属类别             |
    | `goods_cate_id` | `bigint`   |              | ✔    |      | 店内所属分类             |
    | `press`         | `varcahr`  | 128          | ✔    |      | 出版社                   |
    | `pub_date`      | `datetime` |              |      |      | 出版时间                 |
    | `size`          | `varchar`  | 32           |      |      | 开本                     |
    | `pages`         | `int`      |              |      |      | 页数                     |
    | `inventory`     | `int`      |              |      |      | 库存                     |
    | `status`        | `int`      |              |      |      | 售卖状态，0-禁售；1-启售 |
    | `mba`           | `int`      |              |      |      | 月借阅量                 |
    | `tba`           | `int`      |              |      |      | 总借阅量                 |
    | `score`         | `decimal`  | 2[1]         |      |      | 评分                     |
    | `create_time`   | `datetime` |              |      |      | 创建时间                 |
    | `update_time`   | `datetime` |              |      |      | 更新时间                 |
    | `create_user`   | `datetime` |              |      |      | 创建者                   |
    | `update_user`   | `datetime` |              |      |      | 更新者                   |
    | `is_delete`     | `int`      |              |      |      | 是否删除，0-默认；1-删除 |
  
- `book_info` — 图书版权信息表
  - | 字段名        | 类型      | 长度[小数位] | 非空 | 键   | 备注               |
    | ------------- | --------- | ------------ | ---- | ---- | ------------------ |
    | `id`          | `bigint`  |              | ✔    | 🔑    | 主键，版权信息`id` |
    | `book_id`     | `bigint`  |              | ✔    |      | 图书`id`           |
    | `isbn`        | `varchar` | 32           |      |      | 图书`ISBN`         |
    | `bar_code`    | `varchar` | 64           |      |      | 条形码             |
    | `binding`     | `varchar` | 16           |      |      | 装帧               |
    | `version`     | `int`     |              |      |      | 版次               |
    | `volume`      | `int`     |              |      |      | 册数               |
    | `weight`      | `int`     |              |      |      | 重量               |
    | `print_times` | `int`     |              |      |      | 印刷次数           |
    
    
  
- `book_detail` — 图书详情表

  - | 字段名         | 类型      | 长度[小数位] | 非空 | 键   | 备注               |
    | -------------- | --------- | ------------ | ---- | ---- | ------------------ |
    | `id`           | `bigint`  |              | ✔    | 🔑    | 主键，图书详情`id` |
    | `book_id`      | `bigint`  |              | ✔    |      | 图书`id`           |
    | `feature_desc` | `varchar` | 4096         |      |      | 本书特色           |
    | `outline`      | `varchar` | 4096         |      |      | 图书简介           |
    | `author_desc`  | `varchar` | 4096         |      |      | 作者简介           |
    
    
  
- `book_cost` — 图书费用表

  - | 字段名         | 类型      | 长度[小数位] | 非空 | 键   | 备注                         |
    | -------------- | --------- | ------------ | ---- | ---- | ---------------------------- |
    | `id`           | `bigint`  |              | ✔    | 🔑    | 主键，图书费用`id`           |
    | `book_id`      | `bigint`  |              | ✔    |      | 图书`id`                     |
    | `borrow_cost`  | `decimal` | 10[2]        |      |      | 借阅费，元/天                |
    | `free_day`     | `int`     |              |      |      | 免借阅费天数                 |
    | `packing_cost` | `decimal` | 10[2]        |      |      | 包装费，元                   |
    | `deposit`      | `decimal` | 10[2]        |      |      | 押金，即图书价格；还书后退还 |
    
    
  
- `book_cate` — 图书类别表

  - | 字段名 | 类型      | 长度[小数位] | 非空 | 键   | 备注           |
    | ------ | --------- | ------------ | ---- | ---- | -------------- |
    | `id`   | `bigint`  |              | ✔    | 🔑    | 主键，类别`id` |
    | `name` | `varchar` | 16           | ✔    |      | 类别名称       |
    | `sort` | `int`     |              |      |      | 排序依据，权值 |



## 订单表

- `order` — 订单表

  - | 字段名            | 类型       | 长度[小数位] | 非空 | 键   | 备注                                                         |
    | ----------------- | ---------- | ------------ | ---- | ---- | ------------------------------------------------------------ |
    | `id`              | `bigint`   |              | ✔    | 🔑    | 主键，订单`id`                                               |
    | `number`          | `varchar`  | 24           | ✔    |      | 订单号                                                       |
    | `user_id`         | `bigint`   |              | ✔    |      | 用户`id`                                                     |
    | `store_id`        | `bigint`   |              | ✔    |      | 书店`id`                                                     |
    | `address_id`      | `bigint`   |              | ✔    |      | 地址`id`                                                     |
    | `consignee`       | `varchar`  | 32           |      |      | 收货人                                                       |
    | `phone`           | `varchar`  | 18           |      |      | 手机号                                                       |
    | `address`         | `varchar`  | 200          |      |      | 地址                                                         |
    | `borrow_time`     | `int`      |              | ✔    |      | 借阅时间(天)                                                 |
    | `renew_duration`  | `int`      |              |      |      | 可续借时长，默认为`30`天                                     |
    | `overdue_time` | `int` | | | | 逾期时间 |
    | `expected_time`   | `datetime` |              |      |      | 期望配送的时间                                               |
    | `return_time` | `datetime` | | | | 预约归还时间 |
    | `recycle_time` | `datetime` | | | | 回收时间 |
    | `delivery_fee`   | `decimal`  |              |      |      | 配送费                                                       |
    | `shipping_method` | `int`      |              |      |      | 配送方式，0-由商家提供，1-由平台配送                         |
    | `pay_method`      | `int`      |              |      |      | 支付方式，0-在线支付                                         |
    | `pay_time`        | `datetime` |              |      |      | 支付时间                                                     |
    | `overdue_cost` | `decimal` | | | | 逾期费用 |
    | `order_amount`    | `decimal`  |              |      |      | 订单总金额                                                   |
    | `discount_ids` |  | | | | 优惠卷列表 |
    | `discount_amount` | `decimal`  |              |      |      | 优惠金额，默认为`0`                                          |
    | `amount`          | `decimal`  |              |      |      | 实付金额 = 订单总金额 - 优惠金额                             |
    | `remark`          | `varchar`  | 216          |      |      | 备注                                                         |
    | `trade_status`    | `int`      |              | ✔    |      | 交易状态，0-进行中；1-已完成；2-已取消                       |
    | `pay_status`      | `int`      |              | ✔    |      | 支付状态，0-未支付，1-已支付                                 |
    | `am_status`       | `int`      |              |      |      | 售后状态，0-待售后；1-待退款；2-已退款；3-待退货；4-已退货   |
    | `status`          | `int`      |              | ✔    |      | 订单状态，0-待配送[^4.1]；1-待收货[^4.2]；2-待归还[^4.3]；3-待上门[^4.4]；4-待评价[^4.5]；5-已完成[^4.6]；6-逾期中[^4.7]；7-已逾期[^4.8]；8-售后中[^4.9] |
    | `is_comment` | `int` | |  | | 评论状态，0-未评论；1-已评论 |
    | `create_time`     | `datetime` |              | ✔    |      | 创建时间                                                     |
    | `update_time`     | `datetime` |              | ✔    |      | 更新时间                                                     |
    | `is_delete`       | `int`      |              |      |      | 是否删除，0-默认；1-删除                                     |



- `order_item` — 订单项详情表

  - | 字段名         | 类型      | 长度[小数位] | 非空 | 键   | 备注                                   |
    | -------------- | --------- | ------------ | ---- | ---- | -------------------------------------- |
    | `id`           | `bigint`  |              | ✔    | 🔑    | 主键，订单(商品)详情`id`               |
    | `order_id`     | `bigint`  |              | ✔    |      | 订单`id`                               |
    | `book_id`      | `bigint`  |              | ✔    |      | 图书`id`                               |
    | `quantity`     | `int`     |              |      |      | 数量                                   |
    | `item_status`  | `int`     |              |      |      | 订单项状态，0-已提交订单；1-已取消订单 |
    | `borrow_cost`  | `decimal` | 10[2]        |      |      | 借阅费                                 |
    | `packing_cost` | `decimal` | 10[2]        |      |      | 包装费                                 |
    | `overdue_cost` | `decimal` | 10[2]        |      |      | 逾期费用                               |
    | `deposit`      | `decimal` | 10[2]        |      |      | 押金                                   |
    | `amount`       | `decimal` | 10[2]        |      |      | 总金额                                 |
    | `is_comment`   | int       |              |      |      | 评论状态，0-未评论；1-已评论           |

- `after_sales` — 售后单据表
  - | 字段名          | 类型       | 长度[小数位] | 非空 | 键   | 备注                                                         |
    | --------------- | ---------- | ------------ | ---- | ---- | ------------------------------------------------------------ |
    | `id`            | `bigint`   |              | ✔    | 🔑    | 主键，售后`id`                                               |
    | `order_id`      | `bigint`   |              | ✔    |      | 订单ID                                                       |
    | `order_status`  | `int`      |              |      |      | 订单售后时状态：0-待配送；1-待收货；2-待归还；3-待上门；4-已上门(待评价)；5-已完成；6-逾期中；7-已逾期 |
    | `type`          | `int`      |              |      |      | 售后类型：0-退款；1-退货退款；2-换货(暂不设计实现)           |
    | `status`        | `int`      |              |      |      | 售后状态：0-审核中；1-待退货；2-待回收；3-待退款；4-已退货；5-已退款；6-待换货；7-售后已结束 |
    | `reason`        | `varchar`  | 255          |      |      | 售后原因：退款原因、换货原因等                               |
    | `fail_reason`   | `varchar`  | 255          |      |      | 售后失败原因                                                 |
    | `proof`         | `varchar`  | 255          |      |      | 售后补充凭证(图片信息)                                       |
    | `refund_amount` | `decimal`  | 10[2]        |      |      | 退款金额                                                     |
    | `refund_time`   | `datetime` |              |      |      | 退款时间                                                     |
    | `agree_time`    | `datetime` |              |      |      | 审核通过时间                                                 |
    | `return_mode`   | `int`      |              |      |      | 退货模式：0-7天期限；1-双倍期限；2-无期限<br/>(7天为默认配置，可在项目文档中配置) |
    | `return_time`   | `datetime` |              |      |      | 退货预约时间                                                 |
    | `recycle_time`  | `datetime` |              |      |      | 退货回收时间                                                 |
    | `create_time`   | `datetime` |              |      |      | 创建时间                                                     |
    | `update_time`   | `datetime` |              |      |      | 更新时间                                                     |
    | `is_deleted`    | `int`      |              |      |      | 是否删除，0-默认；1-删除                                     |
  
- `comments` — 评论表

  - | 字段名          | 类型       | 长度[小数位] | 非空 | 键   | 备注                     |
    | --------------- | ---------- | ------------ | ---- | ---- | ------------------------ |
    | `id`            | `bigint`   |              | ✔    | 🔑    | 主键，评论`id`           |
    | `order_id`      | `bigint`   |              | ✔    |      | 订单`id`                 |
    | `order_item_id` | `bigint`   |              | ✔    |      | 订单详情`id`             |
    | `user_id`       | `bigint`   |              | ✔    |      | 用户`id`                 |
    | `book_id`       | `bigint`   |              | ✔    |      | 图书`id`                 |
    | `store_id`      | `bigint`   |              | ✔    |      | 书店`id`                 |
    | `content`       | `varchar`  | 216          |      |      | 评论内容                 |
    | `score`         | `int`      | 2[1]         |      |      | 评分                     |
    | `is_anonymous`  | `int`      |              |      |      | 是否匿名，0-不匿；1-匿名 |
    | `create_time`   | `datetime` |              |      |      | 创建时间                 |
    | `update_time`   | `datetime` |              |      |      | 更新时间                 |
    | `is_delete`     | `int`      |              |      |      | 是否删除，0-默认；1-删除 |



- `reply` — 回复表(现阶段只有商家回复)

  - | 字段名        | 类型       | 长度[小数位] | 非空 | 键   | 备注                     |
    | ------------- | ---------- | ------------ | ---- | ---- | ------------------------ |
    | `id`          | `bigint`   |              | ✔    | 🔑    | 主键，回复`id`           |
    | `comment_id`  | `bigint`   |              | ✔    |      | 父评论`id`               |
    | `store_id`    | `bigint`   |              |      |      | 书店`id`                 |
    | `user_id`     | `bigint`   |              |      |      | 用户`id`                 |
    | `content`     | `varchar`  | 216          |      |      | 回复内容                 |
    | `is_deleted`  | `int`      |              |      |      | 是否删除，0-默认；1-删除 |
    | `create_time` | `datetime` |              |      |      | 创建时间                 |
    | `update_time` | `datetime` |              |      |      | 更新时间                 |



## 购物车表

- `cart` — 购物车表

  - | 字段名         | 类型       | 长度[小数位] | 非空 | 键   | 备注                         |
    | -------------- | ---------- | ------------ | ---- | ---- | ---------------------------- |
    | `id`           | `bigint`   |              | ✔    | 🔑    | 主键，购物车`id`             |
    | `user_id`      | `bigint`   |              | ✔    |      | 用户`id`                     |
    | `book_id`      | `bigint`   |              | ✔    |      | 图书`id`                     |
    | `store_id`     | `bigint`   |              | ✔    |      | 商家`id`                     |
    | `borrow_time`  | `int`      |              |      |      | 借阅时长                     |
    | `borrow_cost`  | `decimal`  | 10[2]        |      |      | 借阅费                       |
    | `packing_cost` | `decimal`  | 10[2]        |      |      | 包装费                       |
    | `deposit`      | `decimal`  | 10[2]        |      |      | 押金                         |
    | `amount`       | `decimal`  | 10[2]        |      |      | 总金额                       |
    | `quantity`     | `int`      |              |      |      | 数量                         |
    | `checked`      | `int`      |              |      |      | 是否被选中，0-未选中；1-选中 |
    | `create_time`  | `datetime` |              |      |      | 创建时间                     |
    | `update_time`  | `datetime` |              |      |      | 更新时间                     |

## 地址表

- `address` — 地址表

  - | 字段名        | 类型       | 长度[小数位] | 非空 | 键   | 备注                                                         |
    | ------------- | ---------- | ------------ | ---- | ---- | ------------------------------------------------------------ |
    | `id`          | `bigint`   |              | ✔    | 🔑    | 主键，地址`id`                                               |
    | `user_id`     | `bigint`   |              | ✔    |      | 用户`id`                                                     |
    | `consignee`   | `varchar`  | 32           | ✔    |      | 收货人姓名                                                   |
    | `sex`         | `int`      |              | ✔    |      | 性别称呼，0-先生；1-女士                                     |
    | `area_code`   | `varchar`  | 12           | ✔    |      | 区号，+86(默认，中国大陆)，+866(中国台湾)，+852(中国香港)，+853(中国澳门) |
    | `phone`       | `varchar`  | 24           | ✔    |      | 手机号                                                       |
    | `location`    | `varchar`  | 128          | ✔    |      | 地址定位                                                     |
    | `detail`      | `varchar`  | 128          |      |      | 详细地址                                                     |
    | `label`       | `varchar`  | 16           |      |      | 标签，家、学习、公司等                                       |
    | `is_default`  | `int`      |              |      |      | 是否默认，0-否；1-是                                         |
    | `create_time` | `datetime` |              |      |      | 创建时间                                                     |
    | `update_time` | `datetime` |              |      |      | 更新时间                                                     |



## 收藏表

- `collection` — 收藏商家表

  - | 字段名        | 类型       | 长度[小数位] | 非空 | 键   | 备注               |
    | ------------- | ---------- | ------------ | ---- | ---- | ------------------ |
    | `id`          | `bigint`   |              | ✔    | 🔑    | 主键，收藏记录`id` |
    | `user_id`     | `bigint`   |              | ✔    |      | 用户`id`           |
    | `store_id`    | `bigint`   |              | ✔    |      | 商家`id`           |
    | `create_time` | `datetime` |              |      |      | 创造时间           |
    | `update_time` | `datetime` |              |      |      | 更新时间           |





## 其它表

- `link` — 友情链接表

  - | 字段名        | 类型       | 长度[小数位] | 非空 | 键   | 备注                                       |
    | ------------- | ---------- | ------------ | ---- | ---- | ------------------------------------------ |
    | `id`          | `bigint`   |              | ✔    | 🔑    | 主键，链接`id`                             |
    | `name`        | `varchar`  | 108          |      |      | 网站名称                                   |
    | `link`        | `varchar`  | 200          |      |      | 网站链接                                   |
    | `logo`        | `varchar`  | 200          |      |      | 网站`Logo`                                 |
    | `type`        | `varchar`  | 32           |      |      | 网站类型                                   |
    | `email`       | `varchar`  | 200          |      |      | 联系邮箱                                   |
    | `profile`     | `varchar`  | 216          |      |      | 网站简介                                   |
    | `flag`        | `int`      |              |      |      | 审核标志，0-待审核；1-审核通过；2-审核失败 |
    | `create_time` | `datetime` |              |      |      | 创建时间                                   |
    | `update_time` | `datetime` |              |      |      | 更新时间                                   |

- `ls_account` — 流书网流水账户表

  - | 字段名          | 类型       | 长度[小数位] | 非空 | 键   | 备注                                            |
    | --------------- | ---------- | ------------ | ---- | ---- | ----------------------------------------------- |
    | `id`            | `int`      |              | ✔    | 🔑    | 主键，账户`id`                                  |
    | `account_name`  | `varchar`  | 128          |      |      | 账户名称                                        |
    | `fund`          | `decimal`  | 10[2]        |      |      | 账户流动资金                                    |
    | `order_revenue` | `decimal`  | 10[2]        |      |      | 订单收入(平台收入-平台会从每笔订单收取一定费用) |
    | `other_income`  | `decimal`  | 10[2]        |      |      | 其它收入                                        |
    | `create_time`   | `datetime` |              |      |      | 创建时间                                        |
    | `update_time`   | `datetime` |              |      |      | 更新时间                                        |

- `user_preference` — 用户喜好度记录表(用于提供推荐算法数据集)
  
  - | 字段名    | 类型       | 长度[小数位] | 非空 | 键   | 备注     |
    | --------- | ---------- | ------------ | ---- | ---- | -------- |
    | `id`      | `bigint`   |              | ✔    | 🔑    | 主键`id` |
    | `user_id` | `bigint`   |              |      |      | 用户ID   |
    | `book_id` | `bigint`   |              |      |      | 图书ID   |
    | `score`   | `float`    |              |      |      | 喜好度   |
    | `time`    | `datetime` |              |      |      | 时间     |
  
- `statistical` — 记录表(用于记录某些表记录数，主要满足系统某项性能计算设计)
  
  - | 字段名       | 类型      | 长度[小数位] | 非空 | 键   | 备注     |
    | ------------ | --------- | ------------ | ---- | ---- | -------- |
    | `id`         | `bigint`  |              | ✔    | 🔑    | 主键     |
    | `table_name` | `varchar` | 128          | ✔    |      | 表名     |
    | `record`     | `bigint`  |              |      |      | 表记录数 |

## 脚注

[^2.1]: 借阅图书时，不可超过这个时间，书店图书的默认值
[^2.2]: 续借图书时，不可超过这个时间，书店图书的默认值
[^4.1]: 用户提交订单后，商家配送前
[^4.2]: 商家配送中
[^4.3]: 商家配送完成后，借阅期限内
[^4.4]: 用户预约归还后，商家上门回收前
[^4.5]: 商家回收图书，确认无损后，用户可对本单进行评价
[^4.6]: 用户还书后，确认完成订单(用户获得退回押金，商家获得订单收入)
[^4.7]: 用户已逾期，但不超过7天(待用户支付逾期费用后退还押金)
[^4.8]: 用户逾期(超过7天)未归还图书(扣除押金)
[^4.9]: 用户对订单有异议，可申请售后，与商家协商
