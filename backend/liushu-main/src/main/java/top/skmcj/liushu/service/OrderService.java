package top.skmcj.liushu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.skmcj.liushu.dto.OrderDto;
import top.skmcj.liushu.entity.Order;
import top.skmcj.liushu.vo.OrderPageVo;
import top.skmcj.liushu.vo.OrderVo;

import java.time.LocalDateTime;

public interface OrderService extends IService<Order> {

    OrderDto getOrderById(Long id);

    Page getOrderPage(OrderPageVo pageVo, Long storeId);

    OrderDto generateOrder(OrderDto orderDto);

    boolean payOrder(Long orderId);

    boolean ListenerOrder(Long orderId);

    boolean cancelOrder(Long orderId);

    Page<OrderDto> getAllOrderOfPage(Long userId, int currentPage, int pageSize, String imgDoMain);

    Page<OrderDto> getOrderByStatusOfPage(Long userId, int status, int currentPage, int pageSize, String imgDoMain);

    boolean updateOrderStatus(Long orderId, Integer status);

    boolean updateOrderAmStatus(Long orderId, Integer amStatus);

    boolean repayOfOrder(Long orderId, LocalDateTime returnTime);

    int inspectOverdueOrderOfStore(Long storeId);

    int inspectOverdueOrderOfUser(Long userId);

    int inspectOverdueOrder();

    OrderDto getRenewOrder(Order order);

    void payRenewOfOrder(OrderDto orderDto);

    OrderDto repayOverdueOfOrder(Order order);

    void payOverdueOfOrder(OrderDto orderDto);

    boolean refundOfOrder(Long orderId);
}
