package top.skmcj.liushu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.tools.corba.se.idl.constExpr.Or;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.skmcj.liushu.dao.mapper.OrderMapper;
import top.skmcj.liushu.dto.OrderDto;
import top.skmcj.liushu.entity.*;
import top.skmcj.liushu.service.*;
import top.skmcj.liushu.util.BigDecimalUtil;
import top.skmcj.liushu.util.NumberUtil;
import top.skmcj.liushu.util.TimeUtil;
import top.skmcj.liushu.vo.OrderPageVo;
import top.skmcj.liushu.vo.OrderVo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookCostService costService;

    @Autowired
    private BookstoreService storeService;

    @Autowired
    private UserInfoService infoService;

    @Autowired
    private LsAccountService lsAccountService;

    @Autowired
    private AfterSalesService asService;

    /**
     * 逾期缓存期限
     */
    @Value("${liushu.order.overdue-period}")
    private int overduePeriod;

    /**
     * 流书网订单服务费百分比
     */
    @Value("${liushu.account.service-fee}")
    private BigDecimal serviceFee;

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
        OrderDto rOrder = this.getOrderById(order.getId());
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
     * 前台分页获取用户订单
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    @Transactional
    public Page<OrderDto> getAllOrderOfPage(Long userId, int currentPage, int pageSize, String imgDoMain) {
        Page<OrderDto> orderDtoPage = new Page<>();
        Page<Order> orderPage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getUserId, userId);
        orderWrapper.orderByDesc(Order::getUpdateTime);
        // 获取对应页码的用户订单数据
        this.page(orderPage, orderWrapper);
        List<Order> orders = orderPage.getRecords();
        List<OrderDto> orderDtos = null;
        if (orders != null) {
            // 封装为 OrderDto
            orderDtos = orders.stream().map(item -> {
                Bookstore bookstore = storeService.getById(item.getStoreId());
                LambdaQueryWrapper<OrderItem> orderItemWrapper = new LambdaQueryWrapper<>();
                orderItemWrapper.eq(OrderItem::getOrderId, item.getId());
                // 获取订单项
                List<OrderItem> orderItems = orderItemService.list(orderItemWrapper);
                // 获取订单图书封面
                orderItems.stream().forEach(orderItem -> {
                    Book book = bookService.getById(orderItem.getBookId());
                    orderItem.setBookName(book.getName());
                    orderItem.setBookCover(imgDoMain + book.getCover());
                });
                // 封装
                OrderDto orderDto = packingOrderDto(item, orderItems);

                // 订单申请了售后
                if(item.getStatus().equals(8)) {
                    // 获取售后信息
                    LambdaQueryWrapper<AfterSales> asWrapper = new LambdaQueryWrapper<>();
                    asWrapper.eq(AfterSales::getOrderId, item.getId());
                    AfterSales sales = asService.getOne(asWrapper);
                    orderDto.setAfterSales(sales);
                }

                orderDto.setStoreName(bookstore.getStoreName());
                return orderDto;
            }).collect(Collectors.toList());
        }
        orderDtoPage.setRecords(orderDtos);
        orderDtoPage.setTotal(orderPage.getTotal());
        orderDtoPage.setSize(orderPage.getSize());
        orderDtoPage.setCurrent(orderPage.getCurrent());
        return orderDtoPage;
    }

    /**
     * 根据订单状态分页获取对应用户订单
     * @param userId
     * @param status
     *  1 - 待付款 payStatus: 0
     *  2 - 待配送 status: 0、1
     *  3 - 待归还 status: 2、3、6
     *  4 - 待评价 status: 4、7
     *  5 - 退款/售后 status: 8
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    @Transactional
    public Page<OrderDto> getOrderByStatusOfPage(Long userId, int status, int currentPage, int pageSize, String imgDoMain) {
        Page<OrderDto> orderDtoPage = new Page<>();
        Page<Order> orderPage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Order> orderWrapper = getOrderWrapperByStatus(userId, status);
        orderWrapper.orderByDesc(Order::getUpdateTime);
        // 获取对应订单
        this.page(orderPage, orderWrapper);
        List<Order> orders = orderPage.getRecords();
        List<OrderDto> orderDtos = null;
        if (orders != null) {
            // 封装为 OrderDto
            orderDtos = orders.stream().map(item -> {
                Bookstore bookstore = storeService.getById(item.getStoreId());
                LambdaQueryWrapper<OrderItem> orderItemWrapper = new LambdaQueryWrapper<>();
                orderItemWrapper.eq(OrderItem::getOrderId, item.getId());
                // 获取订单项
                List<OrderItem> orderItems = orderItemService.list(orderItemWrapper);
                // 获取订单图书封面
                orderItems.stream().forEach(orderItem -> {
                    Book book = bookService.getById(orderItem.getBookId());
                    orderItem.setBookName(book.getName());
                    orderItem.setBookCover(imgDoMain + book.getCover());
                });
                // 封装
                OrderDto orderDto = packingOrderDto(item, orderItems);
                // 订单申请了售后
                if(item.getStatus().equals(8)) {
                    // 获取售后信息
                    LambdaQueryWrapper<AfterSales> asWrapper = new LambdaQueryWrapper<>();
                    asWrapper.eq(AfterSales::getOrderId, item.getId());
                    AfterSales sales = asService.getOne(asWrapper);
                    orderDto.setAfterSales(sales);
                }

                orderDto.setStoreName(bookstore.getStoreName());
                return orderDto;
            }).collect(Collectors.toList());
        }
        orderDtoPage.setRecords(orderDtos);
        orderDtoPage.setTotal(orderPage.getTotal());
        orderDtoPage.setSize(orderPage.getSize());
        orderDtoPage.setCurrent(orderPage.getCurrent());
        return orderDtoPage;
    }

    /**
     * 订单状态修改
     * @param orderId
     * @param status
     * 订单状态，0-待配送；1-待收货；2-待归还；3-待上门；4-已上门(待评价)；5-已完成；6-逾期中；7-已逾期；8-售后中
     * @return
     */
    @Override
    public boolean updateOrderStatus(Long orderId, Integer status) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(status);
        if(status.equals(1)) {
            // 已送达，设置送达时间
            order.setDeliveryTime(LocalDateTime.now());
        }
        if(status.equals(4)) {
            // 已上门回收，设置实际回收时间
            order.setRecycleTime(LocalDateTime.now());
        }
        boolean flag = this.updateById(order);
        return flag;
    }

    /**
     * 更新订单售后状态
     * @param orderId
     * @param amStatus
     * @return
     */
    @Override
    public boolean updateOrderAmStatus(Long orderId, Integer amStatus) {
        Order order = new Order();
        order.setId(orderId);
        order.setAmStatus(amStatus);
        boolean flag = this.updateById(order);
        return flag;
    }

    /**
     * 预约归还图书
     * @param orderId
     * @param returnTime
     * @return
     */
    @Override
    public boolean repayOfOrder(Long orderId, LocalDateTime returnTime) {
        Order order = new Order();
        order.setId(orderId);
        order.setReturnTime(returnTime);
        // 3-待上门
        order.setStatus(3);
        boolean flag = this.updateById(order);
        return flag;
    }

    /**
     * 检查商家 6-逾期中 订单
     * @param storeId
     */
    @Override
    @Transactional
    public int inspectOverdueOrderOfStore(Long storeId) {
        // 逾期订单数量
        final int[] count = {0};
        // 1-待收货；2-待归还
        // 获取商家所有已配送、待归还订单
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getStoreId, storeId);
        orderWrapper.eq(Order::getStatus, 1);
        orderWrapper.or().eq(Order::getStatus, 2);
        List<Order> orders = this.list(orderWrapper);
        // 检查这些订单是否逾期，已逾期则修改
        orders.stream().forEach(item -> {
            boolean isLate = isLateOrder(item);
            if(isLate) {
                // 已逾期，将状态设置为 6-逾期中
                this.updateOrderStatus(item.getId(), 6);
                count[0] += 1;
            }
        });
        return count[0];
    }

    /**
     * 检查用户 6-逾期中 订单
     * @param userId
     */
    @Override
    @Transactional
    public int inspectOverdueOrderOfUser(Long userId) {
        final int[] count = {0};
        // 1-待收货；2-待归还
        // 获取用户所有已配送、待归还订单
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getUserId, userId);
        orderWrapper.eq(Order::getStatus, 1);
        orderWrapper.or().eq(Order::getStatus, 2);
        List<Order> orders = this.list(orderWrapper);
        // 检查这些订单是否逾期，已逾期则修改
        orders.stream().forEach(item -> {
            boolean isLate = isLateOrder(item);
            if(isLate) {
                // 已逾期，将状态设置为 6-逾期中
                this.updateOrderStatus(item.getId(), 6);
                count[0] += 1;
            }
        });
        return count[0];
    }

    /**
     * 检查平台 6-逾期中 订单
     * @return
     */
    @Override
    public int inspectOverdueOrder() {
        final int[] count = {0};
        // 1-待收货；2-待归还
        // 获取用户所有已配送、待归还订单
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getStatus, 1);
        orderWrapper.or().eq(Order::getStatus, 2);
        List<Order> orders = this.list(orderWrapper);
        // 检查这些订单是否逾期，已逾期则修改
        orders.stream().forEach(item -> {
            boolean isLate = isLateOrder(item);
            if(isLate) {
                // 已逾期，将状态设置为 6-逾期中
                this.updateOrderStatus(item.getId(), 6);
                count[0] += 1;
            }
        });
        return count[0];
    }

    /**
     * 获取续借订单数据
     * @param order
     * @return
     */
    @Override
    @Transactional
    public OrderDto getRenewOrder(Order order) {
        OrderDto orderDto = new OrderDto();
        // 获取订单数据
        Order mOrder = this.getById(order.getId());
        orderDto.setId(mOrder.getId());
        orderDto.setNumber(mOrder.getNumber());
        orderDto.setBorrowTime(order.getBorrowTime());
        // 获取商家信息
        Bookstore store = storeService.getById(mOrder.getStoreId());
        orderDto.setStoreId(store.getId());
        orderDto.setStoreName(store.getStoreName());
        // 获取订单项
        LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(OrderItem::getOrderId, order.getId());
        List<OrderItem> goodsItems = orderItemService.list(itemWrapper);
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal amount = new BigDecimal(0);
        for (OrderItem item : goodsItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(item.getId());
            orderItem.setOrderId(item.getOrderId());
            orderItem.setBookId(item.getBookId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setBorrowCost(item.getBorrowCost());
            orderItem.setPackingCost(item.getPackingCost());
            orderItem.setAmount(item.getAmount());
            orderItem.setDeposit(item.getDeposit());
            // 计算每一项的借阅费
            // 如果超过图书免费借阅时长，则计费
            Book book = bookService.getById(item.getBookId());
            LambdaQueryWrapper<BookCost> costWrapper = new LambdaQueryWrapper<>();
            costWrapper.eq(BookCost::getBookId, item.getBookId());
            BookCost cost = costService.getOne(costWrapper);
            // 剩余免费借阅时长
            int leftFreeDay = cost.getFreeDay() - mOrder.getBorrowTime();
            // leftFreeDay 小于 0 ，表示无免费借阅时长
            orderItem.setBookName(book.getName());
            orderItem.setBookCover(book.getCover());
            if(leftFreeDay <= 0) {
                // 直接计算
                // 订单项单一金额增量
                BigDecimal singleInc = BigDecimalUtil.multiply(cost.getBorrowCost(), order.getBorrowTime());
                // 订单项金额增量
                BigDecimal inc = BigDecimalUtil.multiply(singleInc, item.getQuantity());
                BigDecimal itemBorrowCost = BigDecimalUtil.add(item.getBorrowCost(), inc);
                BigDecimal itemAmount = BigDecimalUtil.add(item.getAmount(), inc);
                amount = BigDecimalUtil.add(amount, inc);
                orderItem.setBorrowCost(itemBorrowCost);
                orderItem.setAmount(itemAmount);
            } else {
                // 有剩余免费时长
                if(order.getBorrowTime() > leftFreeDay) {
                    // 计算借阅费
                    int payDay = order.getBorrowTime() - leftFreeDay;
                    BigDecimal singleInc = BigDecimalUtil.multiply(cost.getBorrowCost(), order.getBorrowTime());
                    BigDecimal inc = BigDecimalUtil.multiply(singleInc, item.getQuantity());
                    BigDecimal itemBorrowCost = BigDecimalUtil.add(item.getBorrowCost(), inc);
                    BigDecimal itemAmount = BigDecimalUtil.add(item.getAmount(), inc);
                    amount = BigDecimalUtil.add(amount, inc);
                    orderItem.setBorrowCost(itemBorrowCost);
                    orderItem.setAmount(itemAmount);
                }
            }
            orderItems.add(orderItem);
        }
        // 续借暂不可用优惠卷，虽然也没有设计优惠卷
        orderDto.setOrderAmount(amount);
        orderDto.setAmount(amount);
        orderDto.setOrderItems(orderItems);
        return orderDto;
    }

    /**
     * 支付续借订单
     * @param orderDto
     * @return
     */
    @Override
    @Transactional
    public void payRenewOfOrder(OrderDto orderDto) {
        Order order = this.getById(orderDto.getId());
        // 整合(数据库Order)与(续借Order)为新的 Order 数据，修改数据库
        // 设置 Order 的 借阅时间 及 金额
        Order sOrder = new Order();
        sOrder.setId(order.getId());
        // 新的借阅时长 = 订单首次借阅时长 + 续借时长
        sOrder.setBorrowTime(order.getBorrowTime() + orderDto.getBorrowTime());
        // 新的订单金额 = 订单原金额 + 续借金额
        sOrder.setOrderAmount(BigDecimalUtil.add(order.getOrderAmount(), orderDto.getOrderAmount()));
        sOrder.setAmount(BigDecimalUtil.add(order.getAmount(), orderDto.getAmount()));
        // 修改可续借时长
        sOrder.setRenewDuration(order.getRenewDuration() - orderDto.getBorrowTime());
        this.updateById(sOrder);
        // 数据：借阅时长、订单金额
        // 设置订单项数据
        List<OrderItem> goodsItems = orderDto.getOrderItems();
        goodsItems.stream().forEach(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(item.getId());
            orderItem.setBorrowCost(item.getBorrowCost());
            orderItem.setAmount(item.getAmount());
            orderItemService.updateById(orderItem);
        });
    }

    /**
     * 生成逾期中订单数据
     * @param order
     * @return
     */
    @Override
    @Transactional
    public OrderDto repayOverdueOfOrder(Order order) {
        OrderDto orderDto = new OrderDto();
        // 订单数据
        Order mOrder = this.getById(order.getId());
        orderDto.setId(mOrder.getId());
        orderDto.setNumber(mOrder.getNumber());
        orderDto.setDeliveryTime(mOrder.getDeliveryTime());
        orderDto.setBorrowTime(mOrder.getBorrowTime());
        orderDto.setReturnTime(order.getReturnTime());
        // 获取商家信息
        Bookstore store = storeService.getById(mOrder.getStoreId());
        orderDto.setStoreId(store.getId());
        orderDto.setStoreName(store.getStoreName());
        // 计算逾期时长
        // 送达时间
        LocalDateTime deliveryTime = mOrder.getDeliveryTime();
        // 到期时间
        LocalDateTime dueTime = deliveryTime.plusDays(mOrder.getBorrowTime());
        // 逾期时长
        long dueDuration = TimeUtil.compareToDay(dueTime, order.getReturnTime());
        orderDto.setOverdueTime((int) dueDuration);
        // 逾期超过界限
        if(dueDuration > overduePeriod) return orderDto;
        // 计算逾期费用
        // 获取订单项
        LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(OrderItem::getOrderId, order.getId());
        List<OrderItem> goodsItems = orderItemService.list(itemWrapper);
        List<OrderItem> orderItems = new ArrayList<>();
        // 逾期费用
        BigDecimal amount = new BigDecimal(0);
        for (OrderItem item : goodsItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(item.getId());
            orderItem.setOrderId(item.getOrderId());
            orderItem.setBookId(item.getBookId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setBorrowCost(item.getBorrowCost());
            orderItem.setPackingCost(item.getPackingCost());
            orderItem.setDeposit(item.getDeposit());
            // 设置图书信息
            Book book = bookService.getById(item.getBookId());
            orderItem.setBookName(book.getName());
            orderItem.setBookCover(book.getCover());
            // 计算逾期耗费
            LambdaQueryWrapper<BookCost> costWrapper = new LambdaQueryWrapper<>();
            costWrapper.eq(BookCost::getBookId, item.getBookId());
            BookCost cost = costService.getOne(costWrapper);
            // 订单项单一金额增量
            BigDecimal singleInc = BigDecimalUtil.multiply(cost.getBorrowCost(), orderDto.getOverdueTime());
            // 订单项金额增量
            BigDecimal inc = BigDecimalUtil.multiply(singleInc, item.getQuantity());
            orderItem.setOverdueCost(inc);
            orderItem.setAmount(BigDecimalUtil.add(item.getAmount(), inc));
            amount = BigDecimalUtil.add(amount, inc);
            // 加入
            orderItems.add(orderItem);
        }
        orderDto.setOrderItems(orderItems);
        orderDto.setOrderAmount(amount);
        orderDto.setAmount(amount);
        orderDto.setOverdueCost(amount);
        return orderDto;
    }

    /**
     * 支付逾期中订单
     * @param orderDto
     */
    @Override
    @Transactional
    public void payOverdueOfOrder(OrderDto orderDto) {
        Order order = this.getById(orderDto.getId());
        // 更新Order
        Order sOrder = new Order();
        sOrder.setId(orderDto.getId());
        // 新的订单金额 = 订单原金额 + 逾期金额
        sOrder.setOrderAmount(BigDecimalUtil.add(order.getOrderAmount(), orderDto.getOverdueCost()));
        sOrder.setAmount(BigDecimalUtil.add(order.getAmount(), orderDto.getOverdueCost()));
        // 逾期费用
        sOrder.setOverdueCost(orderDto.getOverdueCost());
        // 逾期时间
        sOrder.setOverdueTime(orderDto.getOverdueTime());
        sOrder.setReturnTime(orderDto.getReturnTime());
        sOrder.setStatus(3);
        this.updateById(sOrder);
        // 更新订单项
        List<OrderItem> orderItems = orderDto.getOrderItems();
        orderItems.stream().forEach(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(item.getId());
            orderItem.setOverdueCost(item.getOverdueCost());
            orderItem.setAmount(item.getAmount());
            orderItemService.updateById(orderItem);
        });
    }

    /**
     * 标记已逾期订单
     * @param orderId
     */
    @Override
    @Transactional
    public void makeOverdueOfOrder(Long orderId) {
        // 获取订单数据
        Order mOrder = this.getById(orderId);
        // 标记逾期
        Order order = new Order();
        order.setId(orderId);
        // 7-已逾期
        order.setStatus(7);
        this.updateById(order);
        // 商家获取订单收入
        storeService.addIncome(mOrder.getStoreId(), mOrder.getAmount());
        // 平台获取服务费
        lsAccountService.handleOrderIncomeOfLS(mOrder.getAmount(), mOrder.getAmount());
    }

    /**
     * 订单确认完成
     * @param orderId
     */
    @Override
    @Transactional
    public void completeOrder(Long orderId) {
        // 获取订单数据
        Order mOrder = this.getById(orderId);
        // 设置订单状态为 5-已完成
        Order sOrder = new Order();
        sOrder.setId(mOrder.getId());
        sOrder.setStatus(5);
        this.updateById(sOrder);
        // 订单完成，计算商家获得收入
        // 订单押金
        BigDecimal deposit = new BigDecimal(0);
        LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(OrderItem::getOrderId, mOrder.getId());
        List<OrderItem> orderItems = orderItemService.list(itemWrapper);
        for(OrderItem item : orderItems) {
            // 累加订单项押金
            deposit = BigDecimalUtil.add(deposit, item.getDeposit());
        }
        // 商家收入
        BigDecimal income = BigDecimalUtil.subtract(mOrder.getAmount(), deposit);
        // 更新商家收入
        storeService.addIncome(mOrder.getStoreId(), income);
        // 退回押金
        infoService.addMoney(mOrder.getUserId(), deposit);
        // 更新平台收入
        lsAccountService.handleOrderIncomeOfLS(mOrder.getAmount(), income);
    }

    /**
     * 申请售后
     * @param afterSales
     */
    @Override
    @Transactional
    public boolean applyAfterSalesService(AfterSales afterSales) {
        // 获取订单数据
        Order mOrder = this.getById(afterSales.getOrderId());
        Order order = new Order();
        order.setId(afterSales.getOrderId());
        order.setStatus(8);
        boolean uoFlag = this.updateById(order);
        BigDecimal refund = new BigDecimal(0);
        if(mOrder.getStatus().equals(5)) {
            // 订单已完成后申请售后，只需退还商家收入
            BigDecimal deposit = new BigDecimal(0);
            LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
            itemWrapper.eq(OrderItem::getOrderId, mOrder.getId());
            List<OrderItem> orderItems = orderItemService.list(itemWrapper);
            for(OrderItem item : orderItems) {
                // 累加订单项押金
                deposit = BigDecimalUtil.add(deposit, item.getDeposit());
            }
            // 商家收入
            BigDecimal income = BigDecimalUtil.subtract(mOrder.getAmount(), deposit);
            // 实际退款为商家所得，平台所得 2% 服务费不退还
            refund = BigDecimalUtil.multiply(income, BigDecimalUtil.subtract(new BigDecimal(1), serviceFee));
        } else {
            // 返回订单实付金额
            refund = mOrder.getAmount();
        }
        // 生成售后单据
        AfterSales afs = new AfterSales();
        afs.setOrderId(afterSales.getOrderId());
        afs.setOrderStatus(mOrder.getStatus());
        afs.setType(afterSales.getType());
        afs.setRefundAmount(refund);
        afs.setReason(afterSales.getReason());
        afs.setProof(afterSales.getProof());
        boolean sFlag = asService.save(afs);
        return uoFlag && sFlag;
    }

    /**
     * 订单退款
     * @param orderId
     */
    @Override
    @Transactional
    public void refundOfOrder(Long orderId) {
        // 获取订单数据
        Order mOrder = this.getById(orderId);
        // 获取订单售后单据
        LambdaQueryWrapper<AfterSales> salesWrapper = new LambdaQueryWrapper<>();
        salesWrapper.eq(AfterSales::getOrderId, orderId);
        AfterSales sales = asService.getOne(salesWrapper);
        if(sales.getOrderStatus().equals(5)) {
            // 订单已完成后申请售后，只需退还商家收入
            // 计算商家退后收入
            Bookstore store = storeService.getById(mOrder.getStoreId());
            BigDecimal newIncome = BigDecimalUtil.subtract(store.getIncome(), sales.getRefundAmount());
            // 扣除商家收入
            Bookstore bookstore = new Bookstore();
            bookstore.setId(mOrder.getStoreId());
            bookstore.setIncome(newIncome);
            storeService.updateById(bookstore);
        } else {
            // 返回订单实付金额
            // 平台扣除
            lsAccountService.reduceFundOfLS(sales.getRefundAmount());
        }
        // 将订单金额返还给用户余额
        LambdaQueryWrapper<UserInfo> infoWrapper = new LambdaQueryWrapper<>();
        infoWrapper.eq(UserInfo::getUserId, mOrder.getUserId());
        UserInfo info = infoService.getOne(infoWrapper);
        UserInfo sInfo = new UserInfo();
        sInfo.setId(info.getId());
        sInfo.setMoney(BigDecimalUtil.add(info.getMoney(), sales.getRefundAmount()));
        infoService.updateById(sInfo);
        // 关闭售后
        AfterSales afs = new AfterSales();
        afs.setId(sales.getId());
        afs.setStatus(7);
        asService.updateById(afs);
        // 退款 - 将订单 amStatus 设为 2-已结束
        Order order = new Order();
        order.setId(orderId);
        order.setAmStatus(2);
        this.updateById(order);
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

    /**
     * 根据状态返回对应条件Wrapper
     * @param userId
     * @param status
     *  1 - 待付款 payStatus: 0
     *  2 - 待配送 status: 0、1
     *  3 - 待归还 status: 2、3、6
     *  4 - 待评价 status: 4、7
     *  5 - 退款/售后 status: 8
     * @return
     */
    private LambdaQueryWrapper<Order> getOrderWrapperByStatus(Long userId, int status) {
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        if(status == 1) {
            orderWrapper.eq(Order::getUserId, userId);
            orderWrapper.eq(Order::getTradeStatus, 0);
            orderWrapper.eq(Order::getPayStatus, 0);
        } else if(status == 2) {
            orderWrapper.eq(Order::getUserId, userId);
            orderWrapper.eq(Order::getTradeStatus, 1);
            orderWrapper.eq(Order::getPayStatus, 1);
            orderWrapper.eq(Order::getStatus, 0);
            orderWrapper.or().eq(Order::getStatus, 1);
        } else if(status == 3) {
            orderWrapper.eq(Order::getUserId, userId);
            orderWrapper.eq(Order::getTradeStatus, 1);
            orderWrapper.eq(Order::getPayStatus, 1);
            orderWrapper.eq(Order::getStatus, 2);
            orderWrapper.or().eq(Order::getStatus, 3);
            orderWrapper.or().eq(Order::getStatus, 6);
        } else if(status == 4) {
            orderWrapper.eq(Order::getUserId, userId);
            orderWrapper.eq(Order::getTradeStatus, 1);
            orderWrapper.eq(Order::getPayStatus, 1);
            orderWrapper.eq(Order::getStatus, 4);
            orderWrapper.or().eq(Order::getStatus, 7);
        } else if(status == 5) {
            orderWrapper.eq(Order::getUserId, userId);
            orderWrapper.eq(Order::getTradeStatus, 1);
            orderWrapper.eq(Order::getPayStatus, 1);
            orderWrapper.eq(Order::getStatus, 8);
        } else {
            // 默认搜索全部
            orderWrapper.eq(Order::getUserId, userId);
        }
        return orderWrapper;
    }

    /**
     * 判断订单是否逾期指定期限
     * @param order 订单数据
     * @return
     */
    private boolean isLateOrder(Order order) {
        // 送达时间
        LocalDateTime deliveryTime = order.getDeliveryTime();
        if(deliveryTime == null) {
            // 尚未配送
            return false;
        }
        // 逾期时间
        LocalDateTime overdueTime = deliveryTime.plusDays(order.getBorrowTime());
        // 当前时间是否超过逾期时间
        boolean flag = TimeUtil.gt(LocalDateTime.now(), overdueTime);
        return flag;
    }
}
