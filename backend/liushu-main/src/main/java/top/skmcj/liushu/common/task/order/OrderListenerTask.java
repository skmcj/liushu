package top.skmcj.liushu.common.task.order;

import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.skmcj.liushu.entity.Order;
import top.skmcj.liushu.service.OrderService;

/**
 * 预订单监听任务
 */
@Slf4j
public class OrderListenerTask implements TimerTask {

    /**
     * 订单ID
     */
    private Long orderId;

    @Autowired
    private OrderService orderService;

    public OrderListenerTask(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 超时取消
     * @param timeout
     * @throws Exception
     */
    @Override
    public void run(Timeout timeout) throws Exception {
        log.info("[timer task] 订单 ID-{} 超时未支付，正在取消···", orderId);
        orderService.cancelOrder(orderId);
        // 取消订单
        log.info("[timer task] 订单 ID-{} 超时未支付，取消成功", orderId);
    }
}
