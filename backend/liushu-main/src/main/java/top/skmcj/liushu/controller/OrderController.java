package top.skmcj.liushu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.tools.corba.se.idl.constExpr.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.enums.StatusCodeEnum;
import top.skmcj.liushu.dto.OrderDto;
import top.skmcj.liushu.entity.AfterSales;
import top.skmcj.liushu.entity.Employee;
import top.skmcj.liushu.entity.Order;
import top.skmcj.liushu.service.AfterSalesService;
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

    @Autowired
    private AfterSalesService asService;

    @Value("${liushu.order.confirm-buff-day}")
    private int confirmBuffDay;

    /**
     * 逾期缓存期限
     */
    @Value("${liushu.order.overdue-period}")
    private int overduePeriod;

    /**
     * 售后订单退货预约上门时间缓冲期限
     */
    @Value("${liushu.order.aso-day}")
    private int asoDay;

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

    /**
     * 同意售后单据
     * @param afterSales
     * @return
     */
    @Transactional
    @PutMapping("/after/status/audit/1")
    public Result<String> agreeAfterSalesService(@RequestBody AfterSales afterSales) {
        AfterSales sales = asService.getById(afterSales.getId());
        if(sales == null) return Result.error("查无对应售后单据");
        if(sales.getStatus().equals(7)) return Result.error("售后已关闭结束，无法修改状态");
        if(!sales.getStatus().equals(0)) return Result.error("请勿重复审核售后单据");
        AfterSales afs = new AfterSales();
        afs.setId(sales.getId());
        if(sales.getType().equals(0)) {
            // 退款
            // 3-待退款
            afs.setStatus(3);
        } else if(sales.getType().equals(1)) {
            // 退货退款
            // 1-待退货
            afs.setStatus(1);
        }
        afs.setAgreeTime(LocalDateTime.now());
        asService.updateById(afs);
        // 设置订单售后状态为 1-售后中
        Order order = new Order();
        order.setId(sales.getOrderId());
        order.setAmStatus(1);
        orderService.updateById(order);
        return Result.success("审核意见修改成功");
    }

    /**
     * 拒绝售后单据
     * @param afterSales
     * @return
     */
    @Transactional
    @PutMapping("/after/status/audit/0")
    public Result<String> refuseAfterSalesService(@RequestBody AfterSales afterSales) {
        AfterSales sales = asService.getById(afterSales.getId());
        if(sales == null) return Result.error("查无对应售后单据");
        if(sales.getStatus().equals(7)) return Result.error("售后已关闭结束，无法修改状态");
        if(sales.getStatus().equals(2) || sales.getStatus().equals(3) || sales.getStatus().equals(4))
            return Result.error("售后状态异常，无法修改状态");
        if(sales.getStatus().equals(1)) {
            // 订单待退货
            long day = TimeUtil.compareToDay(sales.getAgreeTime(), LocalDateTime.now());
            // returnMode: 0-7天期限；1-双倍期限；2-无期限
            if(sales.getReturnMode().equals(0) && day <= asoDay) return Result.error("未超过可预约期限，请勿关闭售后");
            else if(sales.getReturnMode().equals(1) && day <= 2 * asoDay) return Result.error("未超过可预约期限，请勿关闭售后");
            else if(sales.getReturnMode().equals(2)) return Result.error("未超过可预约期限，请勿关闭售后");
        }
        AfterSales afs = new AfterSales();
        afs.setId(sales.getId());
        // 结束售后
        afs.setStatus(7);
        afs.setFailReason(afterSales.getFailReason());
        asService.updateById(afs);
        // 设置订单售后状态为 2-已结束
        Order order = new Order();
        order.setId(sales.getOrderId());
        // 订单售后状态 3-已失败
        order.setAmStatus(3);
        orderService.updateById(order);
        return Result.success("审核意见修改成功");
    }

    /**
     * 修改退货模式
     * 0-7天期限；2-双倍期限；3-无期限
     * @param afterSales
     * @return
     */
    @PutMapping("/after/returnMode")
    public Result<String> updateReturnModeOfAfterSales(@RequestBody AfterSales afterSales) {
        AfterSales afs = new AfterSales();
        afs.setId(afterSales.getId());
        afs.setReturnMode(afterSales.getReturnMode());
        boolean flag = asService.updateById(afs);
        if(!flag) return Result.error("退货模式修改失败");
        return Result.success("退货模式修改成功");
    }

    /**
     * 售后确认收到退货(上门收书完成)
     * @param afterSales
     * @return
     */
    @PutMapping("/after/status/receipt")
    public Result<String> confirmReceiptOfASOrder(@RequestBody AfterSales afterSales) {
        AfterSales sales = asService.getById(afterSales.getId());
        if(sales == null) return Result.error("查无对应售后单据");
        if(sales.getStatus().equals(7)) return Result.error("售后已关闭结束，无法修改状态");
        // 设置确认收货
        AfterSales afs = new AfterSales();
        afs.setId(sales.getId());
        // 状态为 3-待退款
        afs.setStatus(3);
        afs.setRecycleTime(LocalDateTime.now());
        boolean flag = asService.updateById(afs);
        if(!flag) return Result.error("收货确认失败");
        return Result.success("收货确认成功");
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
        LambdaQueryWrapper<AfterSales> asWrapper = new LambdaQueryWrapper<>();
        asWrapper.eq(AfterSales::getOrderId, mOrder.getId());
        AfterSales sales = asService.getOne(asWrapper);
        if(sales == null) return Result.error("订单售后状态异常，无法退款");
        if(!sales.getStatus().equals(3)) return Result.error("订单售后状态异常，尚不可退款");
        // 退款
        orderService.refundOfOrder(order.getId());
        return Result.success("订单退款成功");
    }

}
