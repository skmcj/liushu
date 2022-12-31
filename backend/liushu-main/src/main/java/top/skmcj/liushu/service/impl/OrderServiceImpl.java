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
import org.springframework.transaction.annotation.Transactional;
import top.skmcj.liushu.dao.mapper.OrderMapper;
import top.skmcj.liushu.dto.OrderDto;
import top.skmcj.liushu.entity.Book;
import top.skmcj.liushu.entity.Order;
import top.skmcj.liushu.entity.OrderItem;
import top.skmcj.liushu.service.BookService;
import top.skmcj.liushu.service.OrderItemService;
import top.skmcj.liushu.service.OrderService;
import top.skmcj.liushu.util.NumberUtil;
import top.skmcj.liushu.vo.OrderPageVo;
import top.skmcj.liushu.vo.OrderVo;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private BookService bookService;

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
    @Transactional
    public OrderDto generateOrder(OrderDto orderDto) {
        // 生成预订单
        // 生成总订单
        Order order = new Order();
        order.setNumber(NumberUtil.genOrderNumberStr());
        order.setUserId(orderDto.getUserId());
        order.setStoreId(orderDto.getStoreId());
        order.setAddressId(orderDto.getAddressId());
        order.setConsignee(orderDto.getConsignee());
        order.setPhone(orderDto.getPhone());
        order.setAddress(orderDto.getAddress());
        order.setBorrowTime(orderDto.getBorrowTime());
        order.setRenewDuration(orderDto.getRenewDuration());
        order.setExpectedTime(orderDto.getExpectedTime());
        order.setDeliveryFee(orderDto.getDeliveryFee());
        order.setOrderAmount(orderDto.getOrderAmount());
        order.setDiscountAmount(orderDto.getDiscountAmount());
        order.setDiscountIds(orderDto.getDiscountIds());
        order.setAmount(orderDto.getAmount());
        order.setRemark(orderDto.getRemark());
        this.save(order);
        // 生成订单项
        // 扣减相应库存
        List<OrderItem> orderItems = orderDto.getOrderItems();
        orderItems.stream().forEach(item -> {
            item.setOrderId(order.getId());
            orderItemService.save(item);
            Book book = bookService.getById(item.getBookId());
            Book uBook = new Book();
            uBook.setId(item.getBookId());
            uBook.setInventory(book.getInventory() - item.getQuantity());
            bookService.updateById(uBook);
        });
        OrderDto rOrder = this.packingOrderDto(order, orderItems);
        return rOrder;
    }

    /**
     * 支付订单
     * 将订单的支付状态改为1
     * @param orderId
     * @return
     */
    @Override
    public boolean payOrder(Long orderId) {
        // 设置订单为对应状态
        Order order = new Order();
        order.setId(orderId);
        // 交易状态
        order.setTradeStatus(1);
        // 支付状态
        order.setPayStatus(1);
        order.setPayTime(LocalDateTime.now());
        boolean flag = this.updateById(order);
        return flag;
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
    @Transactional
    public boolean cancelOrder(Long orderId) {
        // 订单未支付
        Order order = new Order();
        order.setId(orderId);
        // 交易状态设为取消
        order.setTradeStatus(2);
        this.updateById(order);
        LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(OrderItem::getOrderId, orderId);
        List<OrderItem> orderItems = orderItemService.list(itemWrapper);
        // 设置订单项的状态为取消
        orderItems.stream().forEach(item -> {
            // 返回库存
            Book oldBook = bookService.getById(item.getBookId());
            Book book = new Book();
            book.setId(item.getBookId());
            book.setInventory(oldBook.getInventory() + item.getQuantity());
            bookService.updateById(book);
        });
        return true;
    }

    /**
     * 整合包装订单数据
     * @param order
     * @param orderItems
     * @return
     */
    private OrderDto packingOrderDto(Order order, List<OrderItem> orderItems) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setNumber(order.getNumber());
        orderDto.setUserId(order.getUserId());
        orderDto.setStoreId(order.getStoreId());
        orderDto.setAddressId(order.getAddressId());
        orderDto.setConsignee(order.getConsignee());
        orderDto.setPhone(order.getPhone());
        orderDto.setAddress(order.getAddress());
        orderDto.setBorrowTime(order.getBorrowTime());
        orderDto.setRenewDuration(order.getRenewDuration());
        orderDto.setExpectedTime(order.getExpectedTime());
        orderDto.setDeliveryFee(order.getDeliveryFee());
        orderDto.setShippingMethod(order.getShippingMethod());
        orderDto.setPayMethod(order.getPayMethod());
        orderDto.setPayTime(order.getPayTime());
        orderDto.setOrderAmount(order.getOrderAmount());
        orderDto.setDiscountAmount(order.getDiscountAmount());
        orderDto.setDiscountIds(order.getDiscountIds());
        orderDto.setAmount(order.getAmount());
        orderDto.setRemark(order.getRemark());
        orderDto.setTradeStatus(order.getTradeStatus());
        orderDto.setPayStatus(order.getPayStatus());
        orderDto.setAmStatus(order.getAmStatus());
        orderDto.setStatus(order.getStatus());
        orderDto.setCreateTime(order.getCreateTime());
        orderDto.setUpdateTime(order.getUpdateTime());
        orderDto.setIsDeleted(order.getIsDeleted());
        orderDto.setOrderItems(orderItems);
        return orderDto;
    }
}
