package top.skmcj.liushu.common;

import lombok.Data;
import top.skmcj.liushu.common.enums.StatusCodeEnum;

import java.util.Map;

import static top.skmcj.liushu.common.enums.StatusCodeEnum.*;

/**
 * 结果类
 * @param <T>
 */
@Data
public class Result<T> {
    // 状态
    private Boolean flag;
    // 状态码
    private Integer code;
    // 状态信息
    private String msg;
    // 数据
    private T data;
    // 动态数据
    private Map map;

    /**
     * 成功结果 - default
     * @param <T>
     * @return
     */
    public static <T> Result<T> success() {
        return buildResult(true, null, SUCCESS.getCode(), SUCCESS.getMsg());
    }

    /**
     * 成功结果 - msg
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(String msg) {
        return buildResult(true, msg);
    }

    /**
     * 成功结果 - data
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        return buildResult(true, data, SUCCESS.getCode(), SUCCESS.getMsg());
    }

    /**
     * 成功结果 - statusEnum
     * @param codeEnum
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(StatusCodeEnum codeEnum) {
        return buildResult(true, null, codeEnum.getCode(), codeEnum.getMsg());
    }

    /**
     * 成功结果 - data, status
     * @param data
     * @param codeEnum
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data, StatusCodeEnum codeEnum) {
        return buildResult(true, data, codeEnum.getCode(), codeEnum.getMsg());
    }

    /**
     * 成功结果 - 自定义
     * @param data
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data, Integer code, String msg) {
        return buildResult(true, data, code, msg);
    }

    /**
     * 失败结果 - default
     * @param <T>
     * @return
     */
    public static <T> Result<T> error() {
        return buildResult(false, null, SUCCESS.getCode(), SUCCESS.getMsg());
    }

    /**
     * 失败结果 - msg
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(String msg) {
        return buildResult(false, msg);
    }

    /**
     * 失败结果 - data
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(T data) {
        return buildResult(false, data, SUCCESS.getCode(), SUCCESS.getMsg());
    }

    /**
     * 失败结果 - statusEnum
     * @param codeEnum
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(StatusCodeEnum codeEnum) {
        return buildResult(false, null, codeEnum.getCode(), codeEnum.getMsg());
    }

    /**
     * 失败结果 - data, status
     * @param data
     * @param codeEnum
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(T data, StatusCodeEnum codeEnum) {
        return buildResult(false, data, codeEnum.getCode(), codeEnum.getMsg());
    }

    /**
     * 失败结果 - 自定义
     * @param data
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(T data, Integer code, String msg) {
        return buildResult(false, data, code, msg);
    }

    /**
     * 动态添加数据
     * @param key
     * @param value
     * @return
     */
    public Result<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    /**
     * 简单版
     * 构建结果对象
     * <T> - 表示声明为泛型方法，因为 static 表示静态方法，不属于泛型类一部分，所以需要声明泛型
     *     - 其它方法未声明 static ，属于泛型类一部分，无需声明泛型
     * @param <T>
     * @return
     */
    private static <T> Result<T> buildResult(Boolean flag, String msg) {
        Result<T> r = new Result<>();
        r.setFlag(flag);
        r.setCode(flag ? SUCCESS.getCode() : FAIL.getCode());
        r.setMsg(msg);
        return r;
    }

    /**
     * 完整版
     * 构建结果对象
     * @param <T>
     * @return
     */
    private static <T> Result<T> buildResult(Boolean flag, T data, Integer code, String msg) {
        Result<T> r = new Result<>();
        r.setFlag(flag);
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}
