package top.skmcj.liushu.util;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 时间轮工具
 * 用于处理系统延时任务
 */
@Slf4j
@Component
public class TimingWheelUtil {

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

    /**
     * 添加延时任务
     * @param timerTask
     * @param orderId
     */
    public static void addTimeoutTask(TimerTask timerTask, Long orderId) {
        log.info("[timing wheel] 订单 {} 开始监听", orderId);
        Timeout timeout = wheelTimer.newTimeout(timerTask, delay, TimeUnit.SECONDS);
        timeoutMap.put(orderId, timeout);
    }

    /**
     * 取消延时任务
     * @param orderId
     */
    public static boolean cancelTimeoutTask(Long orderId) {
        if (!timeoutMap.containsKey(orderId)) {
            log.info("[timing wheel] " + orderId + "订单不存在，无法取消");
            return false;
        }
        Timeout timeout = timeoutMap.get(orderId);
        boolean cancel = timeout.cancel();
        if(cancel) {
            // 任务从队列中移除成功后，移除订单id和Timeout的关联关系
            timeoutMap.remove(orderId);
        }
        log.info("[timing wheel] 订单 {} 取消监听", orderId);
        return cancel;
    }
}
