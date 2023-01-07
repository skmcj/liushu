package top.skmcj.liushu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 流书网账户表
 * 主要用户记录流书网平台账户资金情况
 */
@Data
public class LsAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账户ID
     */
    private Integer id;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 账户流动资金
     */
    private BigDecimal fund;

    /**
     * 订单收入
     */
    private BigDecimal orderRevenue;

    /**
     * 其它收入
     */
    private BigDecimal otherIncome;

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
