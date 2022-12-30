package top.skmcj.liushu.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.bo.OrderBo;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.task.order.OrderListenerTask;
import top.skmcj.liushu.service.OrderService;
import top.skmcj.liushu.util.TimingWheelUtil;
import top.skmcj.liushu.vo.OrderVo;

import javax.servlet.http.HttpServletRequest;

/**
 * 前端订单相关
 */
@RestController
@RequestMapping("/frontend/order")
public class FrontendOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 生成订单
     * @param orderVo
     * @param request
     * @return
     */
    @PostMapping
    public Result<String> generateOrder(@RequestBody OrderVo orderVo, HttpServletRequest request) {
        TimingWheelUtil.addTimeoutTask(new OrderListenerTask(orderVo.getId()), orderVo.getId());
        //
        // 检查订单信息是否合法，如数量、金额、商品状态、商家状态
        // HashedWheelTimer
        return Result.success("generate ok" + orderVo.getId());
    }

    /**
     * 取消订单
     * @param orderId
     * @param request
     * @return
     */
    @DeleteMapping("/cancel")
    public Result<String> cancelOrder(Long orderId, HttpServletRequest request) {
        boolean flag = TimingWheelUtil.cancelTimeoutTask(orderId);
        if(!flag) return Result.error("cancel err");
        return Result.success("cancel ok");
    }

    /**
     * 支付订单
     * 平台支付 - 使用账号零钱支付
     * @param orderId
     * @param request
     * @return
     */
    public Result<String> payOrderByLS(Long orderId, HttpServletRequest request) {
        // 获取订单支付金额
        return null;
    }
}
