package top.skmcj.liushu.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.skmcj.liushu.service.OrderService;

/**
 * 监听任务处理工具
 */
@Component
public class ListenerTaskUtil {

    private static OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        ListenerTaskUtil.orderService = orderService;
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    public static boolean cancelOrder(Long orderId) {
        return orderService.cancelOrder(orderId);
    }

}
