package top.skmcj.liushu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.tools.corba.se.idl.constExpr.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.enums.StatusCodeEnum;
import top.skmcj.liushu.dto.OrderDto;
import top.skmcj.liushu.entity.Employee;
import top.skmcj.liushu.entity.Order;
import top.skmcj.liushu.service.OrderService;
import top.skmcj.liushu.util.JwtUtil;
import top.skmcj.liushu.util.TimeUtil;
import top.skmcj.liushu.vo.OrderPageVo;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Value("${liushu.order.confirm-buff-day}")
    private int confirmBuffDay;

    /**
     * 逾期缓存期限
     */
    @Value("${liushu.order.overdue-period}")
    private int overduePeriod;

    /**
     * 分页获取订单
     * @return
     */
    @GetMapping("/page")
    public Result<Page> getOrderPage(OrderPageVo pageVo, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        Page orderPage = orderService.getOrderPage(pageVo, lEmployee.getStoreId());
        return Result.success(orderPage);
    }

    /**
     * 根据id获取订单详情
     * @param id
     * @return
     */
    @GetMapping
    public Result<OrderDto> getOrderById(Long id) {
        OrderDto orderDto = orderService.getOrderById(id);
        return Result.success(orderDto);
    }

    /**
     * 更新订单状态
     * @param status
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/status/{status}")
    public Result<String> updateOrderStatus(@PathVariable Integer status, Long id, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        // 当前登录用户token信息
        Employee lEmployee = JwtUtil.verifyTokenOfEmployee(token);
        if(lEmployee.getCompetence() > 1) {
            return Result.success(StatusCodeEnum.NO_ACCESS);
        }
        Order order = new Order();
        order.setStatus(status);
        order.setId(id);
        orderService.updateById(order);
        return Result.success("订单状态修改成功");
    }

    /**
     * 订单配送完成
     * @param order
     * @return
     */
    @PutMapping("/status/delivery")
    public Result<String> deliveryOfOrder(@RequestBody Order order) {
        boolean flag = orderService.updateOrderStatus(order.getId(), 1);
        if(!flag) return Result.error("配送状态修改失败");
        return Result.success("配送状态修改成功");
    }

    /**
     * 确认收货(配送完成后，用户7天未确认收货时，可点击)
     * @param order
     * @return
     */
    @PutMapping("/status/confirm")
    public Result<String> confirmOfOrder(@RequestBody Order order) {
        // 获取订单对象
        Order mOrder = orderService.getById(order.getId());
        // 期望配送时间与当前时间差值
        long dValue = TimeUtil.compareToDay(mOrder.getDeliveryTime(), LocalDateTime.now());
        if(dValue <= confirmBuffDay) {
            // 配送完成，未超过 7 天
            // 商家无法确认收货，需等待 7 天后或用户自己确认收货
            return Result.error("订单配送完成未超过 7 天，无法确认收货");
        }
        boolean flag = orderService.updateOrderStatus(order.getId(), 2);
        if(!flag) return Result.error("确认收货失败");
        return Result.success("确认收货完成");
    }

    /**
     * 确认完成(上门收书完成后，用户7天未确认完成时，可点击)
     * @param order
     * @return
     */
    @PutMapping("/status/complete")
    public Result<String> confirmComplete(@RequestBody Order order) {
        // 获取订单对象
        Order mOrder = orderService.getById(order.getId());
        if(mOrder == null) return Result.error("查无相关订单");
        // 订单不是 4-已上门，不可确认完成
        if(!mOrder.getStatus().equals(4)) return Result.error("订单状态异常，不可确认完成");
        // 预约归还时间与当前时间差值
        long dValue = TimeUtil.compareToDay(mOrder.getRecycleTime(), LocalDateTime.now());
        if(dValue <= confirmBuffDay) {
            // 上门完成，未超过 7 天
            // 商家无法确认完成，需等待 7 天后或用户自己确认完成
            return Result.error("图书回收完成未超过 7 天，无法确认完成");
        }
        // 订单完成
        orderService.completeOrder(order.getId());
        return Result.success("订单确认完成成功");
    }

    /**
     * 上门收书完成
     * @param order
     * @return
     */
    @PutMapping("/status/recycle")
    public Result<String> recycleOfOrder(@RequestBody Order order) {
        boolean flag = orderService.updateOrderStatus(order.getId(), 4);
        if(!flag) return Result.error("上门收书确认失败");
        return Result.success("上门收书确认完成");
    }

    /**
     * 订单退款
     * @param order
     * @return
     */
    @PutMapping("/status/refund")
    public Result<String> refundOfOrder(@RequestBody Order order) {
        // 获取订单数据
        Order mOrder = orderService.getById(order.getId());
        if(mOrder == null) return Result.error("查无相关订单");
        // 订单未售后，不可退款
        if(!mOrder.getStatus().equals(8)) return Result.error("订单状态异常，不可退款");
        // 退款 - 将订单 amStatus 设为 2
        // 将订单金额返还给用户余额
        return Result.success("订单退款成功");
    }

    /**
     * 设置逾期订单
     * @param order
     * @return
     */
    @PutMapping("/status/overdue")
    public Result<String> beOverdueOfOrder(@RequestBody Order order) {
        // 订单逾期超过缓存期限未归还，商家可标记为逾期
        // 获取订单对象
        Order mOrder = orderService.getById(order.getId());
        if(mOrder == null) return Result.error("查无相关订单");
        // 订单不处于逾期中，无法标记
        if(!mOrder.getStatus().equals(6)) return Result.error("订单状态异常，无法标记逾期");
        // 计算逾期时间
        LocalDateTime deliveryTime = mOrder.getDeliveryTime();
        // 逾期日期
        LocalDateTime overdueDate = deliveryTime.plusDays(mOrder.getBorrowTime());
        // 已逾期时长
        long dueDuration = TimeUtil.compareToDay(overdueDate, LocalDateTime.now());
        // 未超过逾期期限
        if(dueDuration <= overduePeriod) return Result.error("订单尚未超过逾期期限，不可标记逾期");
        // 超过逾期期限
        // 标记逾期，商家获得订单收入
        orderService.makeOverdueOfOrder(order.getId());
        return Result.success("逾期订单标记成功");
    }

}
