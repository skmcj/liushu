package top.skmcj.liushu.vo;

import lombok.Data;

/**
 * 订单支付相关
 */
@Data
public class OrderPayVo {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 支付密码
     */
    private String payPass;
}
