package top.skmcj.liushu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.enums.StatusCodeEnum;
import top.skmcj.liushu.dto.OrderDto;
import top.skmcj.liushu.entity.Employee;
import top.skmcj.liushu.entity.Order;
import top.skmcj.liushu.service.OrderService;
import top.skmcj.liushu.util.JwtUtil;
import top.skmcj.liushu.vo.OrderPageVo;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

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



}
