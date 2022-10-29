package top.skmcj.liushu.common.handler;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.enums.StatusCodeEnum;
import top.skmcj.liushu.common.exception.BusinessException;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 业务异常处理方法
     */
    @ExceptionHandler(BusinessException.class)
    public Result<String> exceptionHandler(BusinessException ex) {
        log.error(ex.getMessage());
        return Result.error(StatusCodeEnum.BUSINESS_HANDLE_ERR.getCode(), ex.getMessage());
    }

    /**
     * JWT相关异常处理
     * JWTDecodeException：header、payload被修改会出现的异常
     * SignatureVerificationException：签名不匹配异常
     * TokenExpiredException：令牌过期异常
     * AlgorithmMismatchException：算法不匹配异常
    */
    @ExceptionHandler(JWTDecodeException.class)
    public Result<String> jwtExceptionHandler(JWTDecodeException ex) {
        log.error(ex.getMessage());
        return Result.error(ex.getMessage(), StatusCodeEnum.JWT_ERR);
    }
    @ExceptionHandler(SignatureVerificationException.class)
    public Result<String> jwtExceptionHandler(SignatureVerificationException ex) {
        log.error(ex.getMessage());
        return Result.error(ex.getMessage(), StatusCodeEnum.JWT_ERR);
    }
    @ExceptionHandler(TokenExpiredException.class)
    public Result<String> jwtExceptionHandler(TokenExpiredException ex) {
        log.error(ex.getMessage());
        return Result.error("token已过期", StatusCodeEnum.JWT_ERR);
    }
    @ExceptionHandler(AlgorithmMismatchException.class)
    public Result<String> jwtExceptionHandler(AlgorithmMismatchException ex) {
        log.error(ex.getMessage());
        return Result.error(ex.getMessage(), StatusCodeEnum.JWT_ERR);
    }


    /**
     * 数据库添加唯一值异常
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> sqlUNExceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());
        return Result.error(StatusCodeEnum.SQL_UNEX_ERR);
    }

    /**
     * 处理所有RuntimeException异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> allRuntimeException(RuntimeException ex) {
        log.error(ex.getMessage());
        Throwable cause = ex.getCause();
        if(cause instanceof SQLIntegrityConstraintViolationException) {
            return Result.error(StatusCodeEnum.SQL_UNEX_ERR);
        }
        return Result.error("运行时异常", StatusCodeEnum.SYSTEM_RUNTIME_ERR);
    }

    /**
     * 处理所有Exception异常
     */
    @ExceptionHandler(Exception.class)
    public Result<String> allException(Exception ex) {
        log.error(ex.getMessage());
        return Result.error("未知异常", StatusCodeEnum.SYSTEM_UNKNOW_ERR);
    }

}
