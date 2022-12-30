package top.skmcj.liushu.common.task.order;

import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

/**
 * 预订单监听任务
 */
@Slf4j
public class OrderListenerTask implements TimerTask {

    /**
     * 订单ID
     */
    private Long orderId;

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
        log.info("[timer task] 订单 {} 超时，正在取消···", orderId);
        Thread.sleep(5000);
        // 取消订单
        log.info("[timer task] 订单 {} 超时，取消成功", orderId);
    }
}
