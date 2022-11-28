package top.skmcj.liushu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户余额变动记录表
 */
@Data
public class UserMoneyRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键。记录ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 类型
     */
    private String type;

    /**
     * 改变类型，0-增加；1-减少
     */
    private Integer change;

    /**
     * 金额
     */
    private BigDecimal amount;

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
