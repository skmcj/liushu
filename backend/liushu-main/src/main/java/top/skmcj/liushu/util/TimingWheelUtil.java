package top.skmcj.liushu.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.skmcj.liushu.common.task.order.OrderListenerTask;
import top.skmcj.liushu.entity.Order;
import top.skmcj.liushu.service.OrderService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 时间轮工具
 * 用于处理系统延时任务
 */
@Slf4j
@Component
public class TimingWheelUtil {

    private static OrderService orderService;

    // 时间轮，1s 一格，共60格
    private static HashedWheelTimer wheelTimer = new HashedWheelTimer(1, TimeUnit.SECONDS, 60);

    // 延时任务列表，便于中途删除任务
    private static Map<Long, Timeout> timeoutMap = new HashMap<>();

    // 延时时间
    private static long delay;

    @Value("${liushu.timing-wheel.delay}")
    public void setDelay(long delay) {
        TimingWheelUtil.delay = delay;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        TimingWheelUtil.orderService = orderService;
    }

    /**
     * 添加延时任务
     * @param timerTask
     * @param orderId
     */
    public static void addTimeoutTask(TimerTask timerTask, Long orderId) {
        log.info("[timing wheel] 订单 ID-{} 开始监听", orderId);
        Timeout timeout = wheelTimer.newTimeout(timerTask, delay, TimeUnit.SECONDS);
        timeoutMap.put(orderId, timeout);
    }

    /**
     * 取消延时任务
     * @param orderId
     */
    public static boolean cancelTimeoutTask(Long orderId) {
        if (!timeoutMap.containsKey(orderId)) {
            log.info("[timing wheel] 订单 ID-{} 不存在，无法取消", orderId);
            return false;
        }
        Timeout timeout = timeoutMap.get(orderId);
        boolean cancel = timeout.cancel();
        if(cancel) {
            // 任务从队列中移除成功后，移除订单id和Timeout的关联关系
            timeoutMap.remove(orderId);
        }
        log.info("[timing wheel] 订单 ID-{} 取消监听", orderId);
        return cancel;
    }

    /**
     * 初始化监听订单
     * 将上次系统突然关闭中断监听的订单恢复监听
     */
    public void initListenerTask() {
        log.info("[timing wheel] 初始化订单监听任务");
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getTradeStatus, 0);
        orderWrapper.eq(Order::getPayStatus, 0);
        // 获取所有未支付预订单
        List<Order> orders = orderService.list(orderWrapper);
        if (orders == null) return;
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.of("+8"));
        long delaySecond = nowSecond - delay;
        orders.stream().forEach(item -> {
            // 判断订单是否还需要监听
            LocalDateTime createTime = item.getCreateTime();
            long cS = createTime.toEpochSecond(ZoneOffset.of("+8"));
            if(cS > delaySecond) {
                // 还未超过 delay 时间，继续监听
                addTimeoutTask(new OrderListenerTask(item.getId()), item.getId());
            } else {
                // 超过 delay 时间，取消订单
                log.info("[timing wheel] 订单 ID-{} 已超时，取消订单", item.getId());
                orderService.cancelOrder(item.getId());
            }
        });
    }
}
