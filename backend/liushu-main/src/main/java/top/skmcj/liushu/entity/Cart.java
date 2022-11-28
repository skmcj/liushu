package top.skmcj.liushu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车表
 */
@Data
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，购物车项ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 图书ID
     */
    private Long bookId;

    /**
     * 商家ID
     */
    private Long storeId;

    /**
     * 借阅时长
     */
    private Integer borrowTime;

    /**
     * 借阅费
     */
    private BigDecimal borrowCost;

    /**
     * 包装费
     */
    private BigDecimal packingCost;

    /**
     * 押金
     */
    private BigDecimal deposit;

    /**
     * 总金额
     */
    private BigDecimal amount;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 是否被选中，0-未选中；1-选中
     */
    private Integer checked;

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
