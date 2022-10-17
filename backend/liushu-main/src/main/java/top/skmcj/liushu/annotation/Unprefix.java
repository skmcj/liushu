package top.skmcj.liushu.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 自定义注解 - 取消路径前缀
 *   - 加上这个注解后，可以取消项目接口前统一的前缀 -> /api
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Unprefix {}
