package top.skmcj.liushu.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.exception.BusinessException;

/**
 * 全局异常处理
 */
@Slf4j
@ResponseBody
@ControllerAdvice(annotations = {RestController.class, ControllerAdvice.class})
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     */
    @ExceptionHandler(BusinessException.class)
    public Result<String> exceptionHandler(BusinessException ex) {
        log.error(ex.getMessage());
        return Result.error(ex.getMessage());
    }
}
