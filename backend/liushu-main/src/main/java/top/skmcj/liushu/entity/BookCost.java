package top.skmcj.liushu.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 图书费用表
 */
@Data
public class BookCost implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 图书费用id
     */
    private Long id;

    /**
     * 所属图书id
     */
    private Long bookId;

    /**
     * 借阅费，元/天
     */
    private BigDecimal borrowCost;

    /**
     * 免借阅费天数
     */
    private Integer freeDay;

    /**
     * 包装费，元
     */
    private BigDecimal packingCost;

    /**
     * 押金，即图书价格，归还图书后退还
     */
    private BigDecimal deposit;

}

