/**
 * ==================
 *  流书网-数据库设计
 *  仅包含表结构
 * ==================
 * 数据库类型：MySQL
 * 数据库名：liushu
 * 数据库版本：8.0.26
 * 默认端口：3306
 * 文件编码：65001
 * 版本：1.0.0
 * 表数量：24
 * ==================
 */

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `id` bigint NOT NULL COMMENT '地址id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `consignee` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `sex` int NULL DEFAULT NULL COMMENT '性别称呼，0-先生；1-女士',
  `area_code` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区号，+86(默认，中国大陆)，+866(中国台湾)，+852(中国香港)，+853(中国澳门)',
  `phone` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `location` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址定位',
  `detail` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `label` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签，家、公司、学校等',
  `is_default` int NULL DEFAULT NULL COMMENT '是否默认，0-否；1-是',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE COMMENT '所属用户的普通索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for after_sales
-- ----------------------------
DROP TABLE IF EXISTS `after_sales`;
CREATE TABLE `after_sales`  (
  `id` bigint NOT NULL COMMENT '售后单ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_status` int NULL DEFAULT NULL COMMENT '订单售后时状态：0-待配送；1-待收货；2-待归还；3-待上门；4-已上门(待评价)；5-已完成；6-逾期中；7-已逾期',
  `type` int NULL DEFAULT 0 COMMENT '售后类型：0-退款；1-退货退款；2-换货(暂不设计实现)',
  `status` int NULL DEFAULT 0 COMMENT '售后状态：0-审核中；1-待退货；2-待回收；3-待退款；4-已退货；5-已退款；6-待换货；7-售后已结束',
  `reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '售后原因：退款原因、换货原因等',
  `fail_reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '售后失败原因',
  `proof` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '售后补充凭证(图片信息)',
  `refund_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '退款金额',
  `refund_time` datetime NULL DEFAULT NULL COMMENT '退款时间',
  `agree_time` datetime NULL DEFAULT NULL COMMENT '审核通过时间',
  `return_mode` int NULL DEFAULT 0 COMMENT '退货模式：0-7天期限；1-双倍期限；2-无期限\r\n(7天为默认配置，可在项目文档中配置)',
  `return_time` datetime NULL DEFAULT NULL COMMENT '退货预约时间',
  `recycle_time` datetime NULL DEFAULT NULL COMMENT '退货回收时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int NULL DEFAULT 0 COMMENT '是否删除，0-默认；1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE COMMENT '订单ID的普通索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '售后单据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `id` bigint NOT NULL COMMENT '图书id',
  `store_id` bigint NOT NULL COMMENT '所属书店id',
  `cover` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '封面',
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图书名称',
  `author` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作者',
  `book_cate_id` bigint NOT NULL COMMENT '图书所属类别',
  `goods_cate_id` bigint NOT NULL COMMENT '所属店内分类',
  `press` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出版社',
  `pub_date` datetime NULL DEFAULT NULL COMMENT '出版时间',
  `size` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开本',
  `pages` int NULL DEFAULT NULL COMMENT '页数',
  `inventory` int NULL DEFAULT NULL COMMENT '库存',
  `status` int NULL DEFAULT 1 COMMENT '售卖状态，0-禁售；1-启售',
  `mba` int NULL DEFAULT 0 COMMENT '月借阅量',
  `tba` int NULL DEFAULT 0 COMMENT '总借阅量',
  `score` decimal(2, 1) NULL DEFAULT NULL COMMENT '评分',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建者',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新者',
  `is_deleted` int NULL DEFAULT 0 COMMENT '是否删除，0-默认；1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_store_id`(`store_id`) USING BTREE COMMENT '所属书店的普通索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '图书表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for book_cate
-- ----------------------------
DROP TABLE IF EXISTS `book_cate`;
CREATE TABLE `book_cate`  (
  `id` int NOT NULL COMMENT '类别id',
  `name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类别名称',
  `sort` int NULL DEFAULT NULL COMMENT '排序依据，权值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '图书类别表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for book_cost
-- ----------------------------
DROP TABLE IF EXISTS `book_cost`;
CREATE TABLE `book_cost`  (
  `id` bigint NOT NULL COMMENT '图书费用id',
  `book_id` bigint NOT NULL COMMENT '所属图书id',
  `borrow_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '借阅费，元/天',
  `free_day` int NULL DEFAULT NULL COMMENT '免借阅费天数',
  `packing_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '包装费，元',
  `deposit` decimal(10, 2) NULL DEFAULT NULL COMMENT '押金，即图书价格，归还图书后退还',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_book_id`(`book_id`) USING BTREE COMMENT '所属图书id的唯一性索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '图书费用表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for book_detail
-- ----------------------------
DROP TABLE IF EXISTS `book_detail`;
CREATE TABLE `book_detail`  (
  `id` bigint NOT NULL COMMENT '图书详情id',
  `book_id` bigint NOT NULL COMMENT '所属图书id',
  `feature_desc` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本书特色',
  `outline` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图书简介',
  `author_desc` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作者简介',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_book_id`(`book_id`) USING BTREE COMMENT '所属图书id的唯一性索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '图书详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for book_info
-- ----------------------------
DROP TABLE IF EXISTS `book_info`;
CREATE TABLE `book_info`  (
  `id` bigint NOT NULL COMMENT '版权信息id',
  `book_id` bigint NOT NULL COMMENT '所属图书id',
  `isbn` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图书ISBN',
  `bar_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '条形码',
  `binding` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '装帧',
  `version` int NULL DEFAULT NULL COMMENT '版次',
  `volume` int NULL DEFAULT NULL COMMENT '册数',
  `weight` int NULL DEFAULT NULL COMMENT '重量',
  `print_times` int NULL DEFAULT NULL COMMENT '印刷次数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_book_id`(`book_id`) USING BTREE COMMENT '所属图书的唯一性索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '图书版权信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bookstore
-- ----------------------------
DROP TABLE IF EXISTS `bookstore`;
CREATE TABLE `bookstore`  (
  `id` bigint NOT NULL COMMENT '书店id、im_id',
  `email` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱',
  `password` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店长员工密码',
  `store_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '书店名称',
  `cover` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '门脸图片',
  `score` decimal(2, 1) NULL DEFAULT NULL COMMENT '评分',
  `address` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店铺地址',
  `business_hours` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '营业时间',
  `distribution` int NULL DEFAULT 0 COMMENT '配送服务，0-由商家配送',
  `delivery_fee` decimal(10, 0) NULL DEFAULT NULL COMMENT '配送费',
  `borrow_day` int NULL DEFAULT NULL COMMENT '默认首次借阅时长',
  `renew_day` int NULL DEFAULT NULL COMMENT '默认可续借时长',
  `store_service` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商家服务',
  `label` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签',
  `notice` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告',
  `income` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '收入',
  `status` int NULL DEFAULT 0 COMMENT '店铺状态。0-休息；1-营业',
  `audit_status` int NULL DEFAULT 0 COMMENT '审核状态，0-未提交；1-审核中；2-通过；3-不通过；4-中途审核',
  `mba` int NULL DEFAULT 0 COMMENT '月借阅量',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `prefix` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_email`(`email`) USING BTREE COMMENT '邮箱的唯一性索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '书店表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bookstore_detail
-- ----------------------------
DROP TABLE IF EXISTS `bookstore_detail`;
CREATE TABLE `bookstore_detail`  (
  `id` bigint NOT NULL COMMENT '书店详情id',
  `store_id` bigint NOT NULL COMMENT '书店id',
  `owner_name` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '法人姓名',
  `id_number` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '法人身份证号',
  `phone` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '法人手机号',
  `business_license` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '营业制作',
  `license_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出版物经营许可证',
  `env_imgs` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店内环境',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_store_id`(`store_id`) USING BTREE COMMENT '对应书店id的唯一性索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '书店详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint NOT NULL COMMENT '主键，购物车项ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `book_id` bigint NOT NULL COMMENT '图书ID',
  `store_id` bigint NOT NULL COMMENT '商家ID',
  `borrow_time` int NULL DEFAULT NULL COMMENT '借阅时长',
  `borrow_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '借阅费',
  `packing_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '包装费',
  `deposit` decimal(10, 2) NULL DEFAULT NULL COMMENT '押金',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '总金额',
  `quantity` int NULL DEFAULT NULL COMMENT '数量',
  `checked` int NULL DEFAULT 0 COMMENT '是否被选中，0-未选中；1-选中',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE COMMENT '用户ID的普通索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for collection
-- ----------------------------
DROP TABLE IF EXISTS `collection`;
CREATE TABLE `collection`  (
  `id` bigint NOT NULL COMMENT '主键，收藏记录ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `store_id` bigint NOT NULL COMMENT '商家ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE COMMENT '用户ID的普通索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint NOT NULL COMMENT '评论id',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `order_item_id` bigint NOT NULL COMMENT '订单详情id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `book_id` bigint NOT NULL COMMENT '图书id',
  `store_id` bigint NOT NULL COMMENT '书店id',
  `content` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论内容',
  `score` decimal(2, 1) NULL DEFAULT NULL COMMENT '评分',
  `is_anonymous` int NULL DEFAULT 0 COMMENT '是否匿名，0-不匿；1-匿名',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int NULL DEFAULT 0 COMMENT '是否删除，0-默认；1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE COMMENT '所属用户的普通索引',
  INDEX `idx_book_id`(`book_id`) USING BTREE COMMENT '所属图书的普通索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` bigint NOT NULL COMMENT '员工id',
  `store_id` bigint NOT NULL COMMENT '书店id',
  `name` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '员工姓名',
  `id_number` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `email` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `sex` int NULL DEFAULT 0 COMMENT '性别，0-未知；1-男；2-女',
  `competence` int NULL DEFAULT NULL COMMENT '权限，0-店长；1-管理员；2-员工',
  `status` int NULL DEFAULT 1 COMMENT '状态，0-禁用；1-正常',
  `is_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除，0-正常；1-删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建者',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un_username`(`username`) USING BTREE COMMENT '用户名唯一索引',
  UNIQUE INDEX `un_email`(`email`) USING BTREE COMMENT '邮箱唯一索引',
  UNIQUE INDEX `un_phone`(`phone`) USING BTREE COMMENT '手机唯一索引',
  INDEX `idx_store_id`(`store_id`) USING BTREE COMMENT '所属书店的普通索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '书店员工表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for goods_cate
-- ----------------------------
DROP TABLE IF EXISTS `goods_cate`;
CREATE TABLE `goods_cate`  (
  `id` bigint NOT NULL COMMENT '分类id',
  `store_id` bigint NOT NULL COMMENT '所属书店id',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分类名称',
  `description` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类描述',
  `sort` int NULL DEFAULT NULL COMMENT '排序依据',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建者',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_store_id`(`store_id`) USING BTREE COMMENT '所属书店的普通索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '书店分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for link
-- ----------------------------
DROP TABLE IF EXISTS `link`;
CREATE TABLE `link`  (
  `id` bigint NOT NULL COMMENT '主键，链接ID',
  `name` varchar(108) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网站名称',
  `link` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网站链接',
  `logo` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网站Logo',
  `email` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系邮箱',
  `profile` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网站简介',
  `type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接类型',
  `flag` int NULL DEFAULT NULL COMMENT '审核标志，0-待审核；1-审核通过；2-审核失败',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '友情链接表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ls_account
-- ----------------------------
DROP TABLE IF EXISTS `ls_account`;
CREATE TABLE `ls_account`  (
  `id` int NOT NULL COMMENT '账户ID',
  `account_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户名称',
  `fund` decimal(10, 2) NULL DEFAULT NULL COMMENT '账户流动资金',
  `order_revenue` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单收入',
  `other_income` decimal(10, 2) NULL DEFAULT NULL COMMENT '其它收入',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '流书网账户表' ROW_FORMAT = Dynamic;

INSERT INTO `liushu`.`ls_account` (`id`, `account_name`, `fund`, `order_revenue`, `other_income`, `create_time`, `update_time`) VALUES (37, '流书网', 370000.00, 0.00, 0.00, '2023-01-07 15:05:34', '2023-02-01 23:29:28');

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` bigint NOT NULL COMMENT '订单id',
  `number` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '所属用户id',
  `store_id` bigint NOT NULL COMMENT '所属书店id',
  `address_id` bigint NOT NULL COMMENT '订单地址id',
  `consignee` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `phone` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人联系方式',
  `address` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货地址',
  `borrow_time` int NULL DEFAULT NULL COMMENT '借阅时长(天)',
  `renew_duration` int NULL DEFAULT NULL COMMENT '可续借时长，默认为图书设定值',
  `overdue_time` int NULL DEFAULT 0 COMMENT '逾期时长',
  `expected_time` datetime NULL DEFAULT NULL COMMENT '期望配送时间',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '实际送达时间',
  `return_time` datetime NULL DEFAULT NULL COMMENT '预约归还时间',
  `recycle_time` datetime NULL DEFAULT NULL COMMENT '实际上门回收时间',
  `delivery_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '配送费',
  `shipping_method` int NULL DEFAULT 0 COMMENT '配送方式，0-由商家配送；1-由平台配送',
  `pay_method` int NULL DEFAULT 0 COMMENT '支付方式，0-在线支付',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `overdue_cost` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '逾期费用',
  `order_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单总金额',
  `discount_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠卷列表',
  `discount_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '优惠金额，默认为0',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '实付金额 = 订单总金额 - 优惠金额',
  `remark` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `trade_status` int NULL DEFAULT 0 COMMENT '交易状态，0-进行中；1-已完成；2-已取消',
  `pay_status` int NULL DEFAULT 0 COMMENT '支付状态，0-未支付，1-已支付',
  `am_status` int NULL DEFAULT 0 COMMENT '售后状态，0-待售后；1-售后中；2-已结束；3-售后失败',
  `status` int NULL DEFAULT 0 COMMENT '订单状态，0-待配送；1-待收货；2-待归还；3-待上门；4-已上门(待评价)；5-已完成；6-逾期中；7-已逾期；8-售后中',
  `is_comment` int NULL DEFAULT 0 COMMENT '评论状态，0-未评论；1-已评论',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int NULL DEFAULT 0 COMMENT '是否删除，0-默认；1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE COMMENT '所属用户的普通索引',
  INDEX `idx_store_id`(`store_id`) USING BTREE COMMENT '所属书店的普通索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` bigint NOT NULL COMMENT '订单详情id',
  `order_id` bigint NOT NULL COMMENT '所属订单id',
  `book_id` bigint NOT NULL COMMENT '图书id',
  `quantity` int NULL DEFAULT NULL COMMENT '数量',
  `item_status` int NULL DEFAULT 0 COMMENT '订单项状态，0-已提交订单；1-已取消订单(暂废)',
  `borrow_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '借阅费',
  `packing_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '包装费',
  `overdue_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '逾期费用',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '总金额',
  `deposit` decimal(10, 2) NULL DEFAULT NULL COMMENT '押金',
  `is_comment` int NULL DEFAULT 0 COMMENT '评论状态，0-未评论；1-已评论',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE COMMENT '所属订单普通索引',
  INDEX `idx_book_id`(`book_id`) USING BTREE COMMENT '该项图书id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for reply
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply`  (
  `id` bigint NOT NULL COMMENT '回复id',
  `comment_id` bigint NOT NULL COMMENT '父评论id',
  `store_id` bigint NULL DEFAULT NULL COMMENT '书店id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `content` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回复内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int NULL DEFAULT 0 COMMENT '是否删除，0-默认；1-删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_comment_id`(`comment_id`) USING BTREE COMMENT '父评论ID'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '回复表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for statistical
-- ----------------------------
DROP TABLE IF EXISTS `statistical`;
CREATE TABLE `statistical`  (
  `id` int NOT NULL COMMENT '表主键',
  `table_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表名',
  `record` bigint NULL DEFAULT 0 COMMENT '表的记录数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '表记录统计表' ROW_FORMAT = Dynamic;

INSERT INTO `liushu`.`statistical` (`id`, `table_name`, `record`) VALUES (1, 'bookstore', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL COMMENT '用户id、im_id(通信服务id)',
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名，字符数字组合，6位及以上',
  `password` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码，MD5加密',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `status` int NULL DEFAULT 1 COMMENT '状态，0-禁用；1-正常，默认为1',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_username`(`username`) USING BTREE COMMENT 'username唯一性索引',
  UNIQUE INDEX `uni_email`(`email`) USING BTREE COMMENT 'email唯一性索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` bigint NOT NULL COMMENT '用户详情记录id',
  `user_id` bigint NOT NULL COMMENT '对应用户id',
  `nickname` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `sex` int NULL DEFAULT 0 COMMENT '性别，0-未知；1-男；2-女',
  `avatar` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `birthday` datetime NULL DEFAULT NULL COMMENT '生日',
  `signature` varchar(216) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个性签名',
  `money` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '账户零钱余额',
  `pay_pass` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付密码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_user_id`(`user_id`) USING BTREE COMMENT '对应用户id的唯一性索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_money_record
-- ----------------------------
DROP TABLE IF EXISTS `user_money_record`;
CREATE TABLE `user_money_record`  (
  `id` bigint NOT NULL COMMENT '主键。记录ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `change` int NULL DEFAULT 0 COMMENT '改变类型，0-增加；1-减少',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '金额',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE COMMENT '用户ID的普通索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户余额变动记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_preference
-- ----------------------------
DROP TABLE IF EXISTS `user_preference`;
CREATE TABLE `user_preference`  (
  `id` bigint NOT NULL COMMENT '喜好记录ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `book_id` bigint NULL DEFAULT NULL COMMENT '图书ID',
  `score` float NULL DEFAULT NULL COMMENT '喜好度',
  `time` datetime NULL DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_uid`(`user_id`) USING BTREE COMMENT '用户ID的普通索引',
  INDEX `idx_bid`(`book_id`) USING BTREE COMMENT '图书ID的普通索引'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户喜好度表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
