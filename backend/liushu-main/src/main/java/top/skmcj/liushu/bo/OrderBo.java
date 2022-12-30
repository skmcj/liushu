package top.skmcj.liushu.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 预订单对象
 */
@Data
@AllArgsConstructor
public class OrderBo {

    /**
     * 订单id
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 支付状态（0 未支付，1已支付，2 取消）
     */
    private Integer payStatus;
    /**
     * 订单创立时间
     */
    private Date createTime;
    /**
     * 订单失效时间
     */
    private Date expireTime;
}
