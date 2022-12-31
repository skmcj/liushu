package top.skmcj.liushu.controller.frontend;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.bo.OrderBo;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.common.enums.StatusCodeEnum;
import top.skmcj.liushu.common.task.order.OrderListenerTask;
import top.skmcj.liushu.dto.OrderDto;
import top.skmcj.liushu.entity.*;
import top.skmcj.liushu.service.*;
import top.skmcj.liushu.util.BigDecimalUtil;
import top.skmcj.liushu.util.JwtUtil;
import top.skmcj.liushu.util.NumberUtil;
import top.skmcj.liushu.util.TimingWheelUtil;
import top.skmcj.liushu.vo.OrderPayVo;
import top.skmcj.liushu.vo.OrderVo;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 前端订单相关
 */
@RestController
@RequestMapping("/frontend/order")
public class FrontendOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookCostService bookCostService;

    @Autowired
    private BookstoreService storeService;

    @Autowired
    private UserInfoService infoService;

    /**
     * 生成订单
     *
     * @param orderVo
     * @param request
     * @return
     */
    @PostMapping
    public Result<OrderDto> generateOrder(@RequestBody OrderVo orderVo, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        User user = JwtUtil.verifyTokenOfUser(token);
        // 检查订单信息是否合法，如数量、金额、商品状态、商家状态
        List<OrderItem> goodsItems = orderVo.getOrderItems();
        List<OrderItem> orderItems = new ArrayList<>();
        boolean bookFlag = false;
        boolean shopFlag = false;
        // 订单总金额
        BigDecimal amount = new BigDecimal(0);
        // 检查商家是否合法
        Bookstore store = storeService.getById(orderVo.getStoreId());
        if (store.getStatus() != 1 || store.getAuditStatus() != 2) {
            // 商家已打烊或资料审核未通过
            shopFlag = true;
        }
        if (shopFlag) return Result.error(StatusCodeEnum.ORDER_GEN_SHOP_ERR);
        // 检查订单项商品是否合法
        for (OrderItem item : goodsItems) {
            Long bookId = item.getBookId();
            Book book = bookService.getById(bookId);
            // 图书是否启售
            if (book.getStatus() == 0) {
                // 图书禁售
                bookFlag = true;
                break;
            }
            // 库存是否充足
            if (book.getInventory() < item.getQuantity()) {
                // 购买数量大于商品库存
                bookFlag = true;
                break;
            }
            LambdaQueryWrapper<BookCost> costWrapper = new LambdaQueryWrapper<>();
            costWrapper.eq(BookCost::getBookId, item.getBookId());
            BookCost bookCost = bookCostService.getOne(costWrapper);
            // 计算金额
            // 借阅费
            BigDecimal itemBorrowCost = new BigDecimal(0);
            if (orderVo.getBorrowTime() > bookCost.getFreeDay()) {
                // 借阅时长超过免费时长
                itemBorrowCost = BigDecimalUtil.multiply(bookCost.getBorrowCost(),
                        (orderVo.getBorrowTime() - bookCost.getFreeDay()));
            }
            // 包装费
            BigDecimal itemPackingCOst = BigDecimalUtil.multiply(bookCost.getPackingCost(), item.getQuantity());
            // 押金
            BigDecimal itemDeposit = BigDecimalUtil.multiply(bookCost.getDeposit(), item.getQuantity());
            // 总金额
            BigDecimal itemAmount = BigDecimalUtil.add(itemBorrowCost, itemPackingCOst, itemDeposit);

            amount = BigDecimalUtil.add(amount, itemAmount);

            OrderItem orderItem = new OrderItem();
            orderItem.setBookId(item.getBookId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setBorrowCost(itemBorrowCost);
            orderItem.setPackingCost(itemPackingCOst);
            orderItem.setAmount(itemAmount);
            orderItem.setDeposit(itemDeposit);
            orderItems.add(orderItem);

        }
        if (bookFlag) return Result.error(StatusCodeEnum.ORDER_GEN_ITEM_ERR);

        // 加上配送费
        amount = BigDecimalUtil.add(amount, store.getDeliverFee());

        // 是否有使用优惠卷
        BigDecimal discountAmount = new BigDecimal(0);
        if (orderVo.getDiscountList() != null && orderVo.getDiscountList().size() > 0) {
            // 有使用优惠卷
            // 计算优惠金额
        }
        // 减去优惠金额
        // BigDecimal realAmount = BigDecimalUtil.subtract(amount, discountAmount);
        BigDecimal realAmount = amount;

        // 订单内容合法
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderItems(orderItems);
        orderDto.setUserId(user.getId());
        orderDto.setStoreId(orderVo.getStoreId());
        orderDto.setAddressId(orderVo.getAddressId());
        orderDto.setConsignee(orderVo.getConsignee());
        orderDto.setPhone(orderVo.getPhone());
        orderDto.setAddress(orderVo.getAddress());
        orderDto.setBorrowTime(orderVo.getBorrowTime());
        orderDto.setRenewDuration(store.getRenewDay());
        orderDto.setExpectedTime(orderVo.getExpectedTime());
        orderDto.setDeliveryFee(store.getDeliverFee());
        orderDto.setOrderAmount(amount);
        orderDto.setDiscountAmount(discountAmount);
        orderDto.setDiscountIds(orderVo.getDiscountIds());
        orderDto.setAmount(realAmount);
        orderDto.setRemark(orderVo.getRemark());
        // 创建订单
        OrderDto rOrder = orderService.generateOrder(orderDto);
        // 监听订单
        TimingWheelUtil.addTimeoutTask(new OrderListenerTask(rOrder.getId()), rOrder.getId());
        return Result.success(rOrder);
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @param request
     * @return
     */
    @DeleteMapping("/cancel")
    public Result<String> cancelOrder(Long orderId, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        User user = JwtUtil.verifyTokenOfUser(token);
        Order order = orderService.getById(orderId);
        if (order == null) return Result.error("查无对应订单");
        if (order.getPayStatus() == 1) return Result.error("订单已支付，无法取消");
        boolean flag = TimingWheelUtil.cancelTimeoutTask(orderId);
        if (!flag) return Result.error("订单取消失败");
        boolean oFlag = orderService.cancelOrder(orderId);
        if (!oFlag) return Result.error("订单取消失败");
        return Result.success("订单取消成功");
    }

    /**
     * 支付订单
     * 平台支付 - 使用账号零钱支付
     *
     * @param orderPayVo
     * @param request
     * @return
     */
    @Transactional
    @PostMapping("/liushu/pay")
    public Result<String> payOrderByLS(@RequestBody OrderPayVo orderPayVo, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        User user = JwtUtil.verifyTokenOfUser(token);
        Order order = orderService.getById(orderPayVo.getOrderId());
        if (order == null) return Result.error("查无对应订单");
        if (order.getTradeStatus() == 2) return Result.error("订单已取消，无法支付");
        if (order.getPayStatus() == 1) return Result.error("订单已支付，请勿重复支付");
        // 获取用户信息
        LambdaQueryWrapper<UserInfo> infoWrapper = new LambdaQueryWrapper<>();
        infoWrapper.eq(UserInfo::getUserId, user.getId());
        UserInfo userInfo = infoService.getOne(infoWrapper);
        // 余额
        BigDecimal balance = userInfo.getMoney();
        String payPass = userInfo.getPayPass();
        // 获取订单支付金额
        BigDecimal amount = order.getAmount();
        if (BigDecimalUtil.lt(balance, amount)) {
            // 用户余额不足以支付
            return Result.error(StatusCodeEnum.ORDER_PAY_NO_MONEY);
        }
        boolean payFlag = orderService.payOrder(orderPayVo.getOrderId());
        if (!payFlag) return Result.error(StatusCodeEnum.ORDER_PAY_ERR);
        // 校验支付密码
        if (payPass == null) return Result.error(StatusCodeEnum.ORDER_PAY_NO_PASS);
        if (!payPass.equals(orderPayVo.getPayPass())) return Result.error(StatusCodeEnum.ORDER_PAY_PASS_ERR);
        // 扣除相应余额
        UserInfo moneyInfo = new UserInfo();
        moneyInfo.setId(userInfo.getId());
        moneyInfo.setMoney(BigDecimalUtil.subtract(balance, amount));
        infoService.updateById(moneyInfo);
        return Result.success(StatusCodeEnum.ORDER_PAY_OK);
    }
}
