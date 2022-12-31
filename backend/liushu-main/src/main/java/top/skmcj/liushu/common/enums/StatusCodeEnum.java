package top.skmcj.liushu.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 接口状态码枚举
 */
@Getter             // 生成成员getter方法
@AllArgsConstructor // 生成全参构造方法
public enum StatusCodeEnum {

    /**
     * 请求成功
     */
    SUCCESS(20000, "请求成功"),
    /**
     * 请求失败
     */
    FAIL(51000, "请求失败"),

    UNKNOW(50010, "未知错误"),
    /**
     * 保存成功、失败
     */
    SAVE_OK(20011, "保存成功"),
    SAVE_ERR(20010, "保存失败"),
    /**
     * 删除成功、失败
     */
    DELETE_OK(20021, "删除成功"),
    DELETE_ERR(20020, "删除失败"),
    /**
     * 修改成功、失败
     */
    UPDATE_OK(20031, "修改成功"),
    UPDATE_ERR(20030, "修改失败"),
    /**
     * 获取成功、失败
     */
    GET_OK(20041, "获取成功"),
    GET_ERR(20040, "获取失败"),
    /**
     * 添加成功、失败
     */
    ADD_OK(20051, "添加成功"),
    ADD_ERR(20050, "添加失败"),
    /**
     * 系统相关状态
     */
    SYSTEM_ERR(52001, "系统异常"),
    SYSTEM_TIMEOUT_ERR(52002, "系统繁忙，请稍后再试！"),
    SYSTEM_RUNTIME_ERR(52003, "系统异常，请检查传参是否有误或稍后重试"),
    SYSTEM_UNKNOW_ERR(52009, "未知错误"),
    /**
     * 业务异常
     */
    BUSINESS_HANDLE_ERR(52201, "业务处理异常"),
    /**
     * 数据库操作异常
     */
    // 重复插入唯一值异常
    SQL_UNEX_ERR(52101, "该用户已存在"),
    /**
     * JWT异常
     */
    JWT_OK(22900, "Token校验正常"),
    JWT_ERR(52901, "Token校验异常"),
    /**
     * 未登录
     */
    NO_LOGIN(40101, "用户未登录"),
    LOGIN_USER_ERR(40102, "用户名错误"),
    LOGIN_PASS_ERR(40103, "密码错误"),
    /**
     * 无权限
     */
    NO_ACCESS(40301, "没有操作权限"),
    /**
     * 用户相关状态
     */
    USER_LOGON_OK(21101, "用户注册成功"),
    USER_LOGIN_OK(21102, "用户登录成功"),
    USER_PAY_OK(21103, "支付密码正确"),
    USERNAME_EXIST(52101, "用户名已存在"),
    USER_EMAIL_EXIST(52102, "邮箱已注册"),
    USERNAME_NOT_EXIST(52103, "用户不存在"),
    USER_STATUS_ERR(52104, "用户已被封禁"),
    USER_PASS_ERR(52105, "用户密码错误"),
    USER_PAY_ERR(52106, "支付密码错误"),
    /**
     * 图片上传状态
     */
    IMAGE_UPLOAD_OK(21001, "图片上传成功"),
    IMAGE_UPLOAD_ERR(21000, "图片上传失败"),
    /**
     * 邮件发送状态
     */
    MAIL_SEND_OK(21011, "邮件发送成功"),
    MAIL_SEND_ERR(21010, "邮件发送失败"),
    /**
     * 验证码校验状态
     */
    CHECK_CODE_OK(21021, "验证码校验成功"),
    CHECK_CODE_ERR(21020, "验证码校验失败"),
    CHECK_CODE_EXPIRED(21022, "验证码已过期"),
    /**
     * 商家注册状态
     */
    BUSINESS_LOGON_OK(21031, "商家注册成功"),
    BUSINESS_LOGON_ERR(21030, "商家注册失败"),
    BUSINESS_LOGON_EXISTS(21032, "商家已注册"),
    /**
     * 商家登录状态
     */
    BUSINESS_LOGIN_OK(21041, "商家登录成功"),
    BUSINESS_LOGIN_ERR(21040, "商家登录失败"),
    BUSINESS_LOGIN_SERR(21042, "账号已被禁用"),
    /**
     * 店铺状态
     */
    STORE_PROCESS_ONS(21052, "店铺审核信息未提交，请前往完善提交"),
    STORE_PROCESS_ENS(21053, "店铺审核信息未提交，请联系店长完善提交"),
    STORE_PROCESS_NP(21054, "店铺审核信息审核未通过"),
    STORE_PROCESS_FP(21050, "店铺审核信息审核不通过"),
    STORE_PROCESS_OK(21051, "店铺审核信息审核通过"),
    STORE_PROCESS_SERR(21055, "店铺审核信息提交成功"),
    STORE_PROCESS_SOK(21056, "店铺审核信息提交失败"),
    /**
     * 商家功能相关
     */
    EMPLOYEE_ADD_OK(21061, "员工添加成功"),
    EMPLOYEE_ADD_ERR(21062, "员工添加失败"),
    EMPLOYEE_EDIT_OK(21063, "员工修改成功"),
    EMPLOYEE_EDIT_ERR(21064, "员工修改失败"),
    EMPLOYEE_DEL_OK(21065, "员工删除成功"),
    EMPLOYEE_DEL_ERR(21066, "员工删除失败"),
    EMPLOYEE_SEARCH_OK(21067, "员工搜索成功"),
    EMPLOYEE_EXISTS(21068, "员工已存在"),

    GOODSCATE_DEL_ERR(21071, "该分类下还有在售图书，不可删除"),

    ORDER_GEN_OK(21080, "订单创建成功"),
    ORDER_GEN_ERR(21081, "订单创建失败"),
    ORDER_GEN_ITEM_ERR(21082, "订单内商品存在不合法状态"),
    ORDER_GEN_SHOP_ERR(21083, "商家未营业"),
    ORDER_PAY_OK(21091, "订单支付成功"),
    ORDER_PAY_ERR(21092, "订单支付失败"),
    ORDER_PAY_NO_MONEY(21093, "用户余额不足"),
    ORDER_PAY_NO_PASS(21094, "用户尚未设置支付密码，无法支付"),
    ORDER_PAY_PASS_ERR(21095, "用户支付密码错误")
    ;

    // 状态码
    private final Integer code;
    // 状态描述
    private final String msg;

}
