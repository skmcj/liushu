package top.skmcj.liushu.common.task.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.skmcj.liushu.service.OrderService;

@Slf4j
@Component
public class OrderHandleTask {

    @Autowired
    private OrderService orderService;

    /**
     * 检查商家订单是否逾期
     * @param storeId
     */
    @Async("orderListenerPool")
    public void inspectOverdueOrderOfStore(Long storeId) {
        log.info("开始商家订单逾期情况检查");
        int count = orderService.inspectOverdueOrderOfStore(storeId);
        log.info("结束商家订单逾期情况检查，此次检查检出逾期订单：{} 单", count);
    }

    /**
     * 检查用户订单是否存在逾期
     * @param userId
     */
    @Async("orderListenerPool")
    public void inspectOverdueOrderOfUser(Long userId) {
        log.info("开始用户订单逾期情况检查");
        int count = orderService.inspectOverdueOrderOfUser(userId);
        log.info("结束用户订单逾期情况检查，此次检查检出逾期订单：{} 单", count);
    }

    /**
     * 检查平台订单是否存在逾期
     */
    @Async("orderListenerPool")
    public void inspectOverdueOrder() {
        log.info("开始平台订单逾期情况检查");
        int count = orderService.inspectOverdueOrder();
        log.info("结束平台订单逾期情况检查，此次检查检出逾期订单：{} 单", count);
    }

}
