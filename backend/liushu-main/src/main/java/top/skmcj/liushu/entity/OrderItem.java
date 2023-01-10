package top.skmcj.liushu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单项表
 */
@Data
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单详情id
     */
    private Long id;

    /**
     * 所属订单id
     */
    private Long orderId;

    /**
     * 图书id
     */
    private Long bookId;

    /**
     * 图书名称
     */
    @TableField(exist = false)
    private String bookName;

    /**
     * 图书封面
     */
    @TableField(exist = false)
    private String bookCover;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 订单项状态，0-已提交订单；1-已取消订单
     */
    private Integer itemStatus;

    /**
     * 借阅费
     */
    private BigDecimal borrowCost;

    /**
     * 包装费
     */
    private BigDecimal packingCost;

    /**
     * 逾期费用
     */
    private BigDecimal overdueCost;

    /**
     * 总金额
     */
    private BigDecimal amount;

    /**
     * 押金
     */
    private BigDecimal deposit;

    /**
     * 商品每日借阅费
     */
    @TableField(exist = false)
    private BigDecimal borrowFee;

    /**
     * 免借阅费天数
     */
    @TableField(exist = false)
    private Integer freeDay;

}
