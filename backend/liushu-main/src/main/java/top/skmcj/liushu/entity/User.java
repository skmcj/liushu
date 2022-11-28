package top.skmcj.liushu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户表
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id、im_id(通信服务id)
     */
    private Long id;

    /**
     * 用户名，字符数字组合，6位及以上
     */
    private String username;

    /**
     * 密码，MD5加密
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 状态，0-禁用；1-正常，默认为1
     */
    private Integer status;

}