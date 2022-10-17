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
     * 系统相关状态
     */
    SYSTEM_ERR(52001, "系统异常"),
    SYSTEM_TIMEOUT_ERR(52002, "系统繁忙，请稍后再试！"),
    SYSTEM_UNKNOW_ERR(52003, "未知错误"),
    /**
     * 未登录
     */
    NO_LOGIN(40101, "用户未登录"),
    /**
     * 无权限
     */
    NO_ACCESS(40301, "没有操作权限"),
    /**
     * 用户相关状态
     */
    USERNAME_EXIST(52101, "用户名已存在"),
    USERNAME_NOT_EXIST(52102, "用户名不存在"),
    /**
     * 图片上传状态
     */
    IMAGE_UPLOAD_OK(21011, "图片上传成功"),
    IMAGE_UPLOAD_ERR(21010, "图片上传失败")
    ;

    // 状态码
    private final Integer code;
    // 状态描述
    private final String msg;

}
