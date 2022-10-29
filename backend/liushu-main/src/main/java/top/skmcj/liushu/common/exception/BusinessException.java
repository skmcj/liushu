package top.skmcj.liushu.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.skmcj.liushu.common.enums.StatusCodeEnum;

/**
 * 自定义业务异常类
 */
@Getter
@AllArgsConstructor
public class BusinessException extends RuntimeException{
    // 错误码
    private Integer code = StatusCodeEnum.FAIL.getCode();

    // 错误信息
    private String message;

    public BusinessException(String message) {
        this.message = message;
    }

    BusinessException(StatusCodeEnum codeEnum) {
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMsg();
    }
}
