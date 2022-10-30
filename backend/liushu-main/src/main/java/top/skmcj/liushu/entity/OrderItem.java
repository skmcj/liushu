package top.skmcj.liushu.entity;

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
    private String bookName;

    /**
     * 图书封面
     */
    private String cover;

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
     * 总金额
     */
    private BigDecimal amount;

}
