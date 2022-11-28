package top.skmcj.liushu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户详情表
 */
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户详情记录id
     */
    private Long id;

    /**
     * 对应用户id
     */
    private Long userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别，0-未知；1-男；2-女
     */
    private Integer sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 头像URL
     */
    @TableField(exist = false)
    private String avatarUrl;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 账户零钱余额
     */
    private BigDecimal money;

    /**
     * 支付密码
     */
    private String payPass;

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

}
