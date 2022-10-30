package top.skmcj.liushu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.skmcj.liushu.dto.OrderDto;
import top.skmcj.liushu.entity.Order;
import top.skmcj.liushu.vo.OrderPageVo;

public interface OrderService extends IService<Order> {

    OrderDto getOrderById(Long id);

    Page getOrderPage(OrderPageVo pageVo, Long storeId);
}
