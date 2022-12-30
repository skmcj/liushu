package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.skmcj.liushu.dao.mapper.OrderMapper;
import top.skmcj.liushu.dto.OrderDto;
import top.skmcj.liushu.entity.Book;
import top.skmcj.liushu.entity.Order;
import top.skmcj.liushu.entity.OrderItem;
import top.skmcj.liushu.service.OrderItemService;
import top.skmcj.liushu.service.OrderService;
import top.skmcj.liushu.vo.OrderPageVo;
import top.skmcj.liushu.vo.OrderVo;

import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderItemService orderItemService;

    /**
     * 根据id获取订单完整信息
     * @param id
     * @return
     */
    @Override
    public OrderDto getOrderById(Long id) {
        OrderDto orderDto = new OrderDto();
        // 获取订单基本信息
        Order order = this.getById(id);
        // 获取订单详情项
        LambdaQueryWrapper<OrderItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderItem::getOrderId, id);
        List<OrderItem> list = orderItemService.list(queryWrapper);
        BeanUtils.copyProperties(order, orderDto);
        orderDto.setOrderItems(list);
        return orderDto;
    }

    /**
     * 获取商家订单数据
     * @param pageVo
     * @param storeId
     * @return
     */
    @Override
    public Page getOrderPage(OrderPageVo pageVo, Long storeId) {
        // 构建分页构造器
        Page pageInfo = new Page(pageVo.getPage(), pageVo.getPageSize());
        // 构建条件构造器
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        // 添加条件
        queryWrapper.eq(Order::getStoreId, storeId);

        queryWrapper.eq(pageVo.getStatus() != null, Order::getStatus, pageVo.getStatus());
        queryWrapper.like(StringUtils.isNotEmpty(pageVo.getConsignee()), Order::getConsignee, pageVo.getConsignee());
        queryWrapper.like(StringUtils.isNotEmpty(pageVo.getNumber()), Order::getNumber, pageVo.getNumber());
        queryWrapper.like(StringUtils.isNotEmpty(pageVo.getPhone()), Order::getPhone, pageVo.getPhone());
        queryWrapper.like(StringUtils.isNotEmpty(pageVo.getAddress()), Order::getAddress, pageVo.getAddress());

        this.page(pageInfo, queryWrapper);

        return pageInfo;
    }

    /**
     * 创建订单
     * @param orderDto
     * @return
     */
    @Override
    public boolean generateOrder(OrderDto orderDto) {
        // 生成预订单
        //
        /** 是否使用优惠卷
         * 使用 - 判断优惠卷是否合法
         */
        // 核算总金额
        // 生成数据库记录 - 订单数据
        // 扣减相应库存
        return false;
    }

    /**
     * 支付订单
     * 将订单的支付状态改为1
     * @param orderId
     * @return
     */
    @Override
    public boolean payOrder(Long orderId) {
        return false;
    }

    /**
     * 监听订单订单
     * @param orderId
     * @return
     */
    @Override
    public boolean ListenerOrder(Long orderId) {
        return false;
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @Override
    public boolean cancelOrder(Long orderId) {
        log.info("取消订单 ==> " + orderId);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("取消完成 ==> " + orderId);
        return false;
    }
}
