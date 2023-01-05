package top.skmcj.liushu.controller.frontend;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import top.skmcj.liushu.util.*;
import top.skmcj.liushu.vo.OrderPayVo;
import top.skmcj.liushu.vo.OrderVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
        amount = BigDecimalUtil.add(amount, store.getDeliveryFee());

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
        orderDto.setDeliveryFee(store.getDeliveryFee());
        orderDto.setOrderAmount(amount);
        orderDto.setDiscountAmount(discountAmount);
        orderDto.setDiscountIds(orderVo.getDiscountIds());
        orderDto.setAmount(realAmount);
        orderDto.setRemark(orderVo.getRemark());
        // 创建订单
        OrderDto rOrder = orderService.generateOrder(orderDto);
        rOrder.setStoreName(store.getStoreName());
        String imgDoMain = CommonUtil.getImgDoMain(request);
        List<OrderItem> rOrderItems = rOrder.getOrderItems();
        // 添加图书信息
        rOrderItems.stream().forEach(item -> {
            Book book = bookService.getById(item.getBookId());
            item.setBookName(book.getName());
            item.setBookCover(imgDoMain + book.getCover());
        });
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
        // 校验支付密码
        if (payPass == null) return Result.error(StatusCodeEnum.ORDER_PAY_NO_PASS);
        if (!payPass.equals(orderPayVo.getPayPass())) return Result.error(StatusCodeEnum.ORDER_PAY_PASS_ERR);
        boolean payFlag = orderService.payOrder(orderPayVo.getOrderId());
        if (!payFlag) return Result.error(StatusCodeEnum.ORDER_PAY_ERR);
        // 扣除相应余额
        UserInfo moneyInfo = new UserInfo();
        moneyInfo.setId(userInfo.getId());
        moneyInfo.setMoney(BigDecimalUtil.subtract(balance, amount));
        infoService.updateById(moneyInfo);
        return Result.success(StatusCodeEnum.ORDER_PAY_OK);
    }

    /**
     * 分页获取用户所有订单
     * @param currentPage
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/all")
    public Result<Page<OrderDto>> getAllOrder(int currentPage, int pageSize, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        User user = JwtUtil.verifyTokenOfUser(token);
        String imgDoMain = CommonUtil.getImgDoMain(request);
        Page<OrderDto> orderPage = orderService.getAllOrderOfPage(user.getId(), currentPage, pageSize, imgDoMain);
        return Result.success(orderPage);
    }

    /**
     * 根据订单状态分页获取用户订单
     * @param status
     *  1 - 待付款 payStatus: 0
     *  2 - 待配送 status: 0、1
     *  3 - 待归还 status: 2、3、6
     *  4 - 待评价 status: 4、7
     *  5 - 退款/售后 status: 8
     * @param currentPage
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/{status}")
    public Result<Page<OrderDto>> getOrderByStatus(@PathVariable int status,
                                                   int currentPage, int pageSize, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        User user = JwtUtil.verifyTokenOfUser(token);
        String imgDoMain = CommonUtil.getImgDoMain(request);
        Page<OrderDto> orderPage = orderService.getOrderByStatusOfPage(user.getId(), status, currentPage, pageSize, imgDoMain);
        return Result.success(orderPage);
    }

    /**
     * 确认收货
     * @param order
     * @return
     */
    @PutMapping("/status/confirm")
    public Result<String> confirmReceipt(@RequestBody Order order) {
        boolean flag = orderService.updateOrderStatus(order.getId(), 2);
        if(!flag) return Result.error("确认收货失败");
        return Result.success("确认收货完成");
    }

    /**
     * 确认完成
     * @param order
     * @return
     */
    @PutMapping("/status/complete")
    public Result<String> confirmComplete(@RequestBody Order order) {
        // 订单完成，商家获得收入
        // 退回押金
        return Result.success("订单确认完成成功");
    }

    /**
     * 续借
     * @param order
     * @return
     */
    @PutMapping("/status/renew")
    public Result<OrderDto> renewOfOrder(@RequestBody Order order, HttpServletRequest request) {
        // 数据库订单数据
        Order mOrder = orderService.getById(order.getId());
        if(mOrder == null) return Result.error("找不到对应订单");
        // 订单状态非 2-待归还 ，不可续借
        if(!mOrder.getStatus().equals(2)) return Result.error("当前订单状态不可续借");
        // 续借时长超过本单剩余可续借时长
        if(order.getBorrowTime().compareTo(mOrder.getRenewDuration()) > 0) {
            return Result.error("续借失败，订单剩余可续借时长不足");
        }
        OrderDto renewOrder = orderService.getRenewOrder(order);
        List<OrderItem> orderItems = renewOrder.getOrderItems();
        String imgDoMain = CommonUtil.getImgDoMain(request);
        orderItems.stream().forEach(item -> {
            item.setBookCover(imgDoMain + item.getBookCover());
        });
        // 整合续借相关 OrderDto ，存入 session ，key 为 orderId
        HttpSession session = request.getSession();
        session.setAttribute(order.getId().toString(), renewOrder);
        // 返回整合的 OrderDto，等待支付
        return Result.success(renewOrder);
    }

    /**
     * 续借订单支付(零钱支付)
     * @param orderPayVo
     * @return
     */
    @PutMapping("/renew/pay")
    public Result<String> renewOfOrderToPayOfLS(@RequestBody OrderPayVo orderPayVo, HttpServletRequest request) {
        Order order = orderService.getById(orderPayVo.getOrderId());
        if(order == null) return Result.error("查无相关订单");
        // 根据 orderId 从 session 中取出订单数据
        HttpSession session = request.getSession();
        OrderDto renewOrder = (OrderDto) session.getAttribute(orderPayVo.getOrderId().toString());
        if(renewOrder == null) return Result.error("查无相关续借订单");
        System.out.println("续借订单金额 ==> " + renewOrder.getAmount());
        System.out.println("续借订单项 ==> " + renewOrder.getOrderItems());

        // 进行支付，从用户零钱减去相应金额
        LambdaQueryWrapper<UserInfo> infoWrapper = new LambdaQueryWrapper<>();
        infoWrapper.eq(UserInfo::getUserId, order.getUserId());
        UserInfo info = infoService.getOne(infoWrapper);
        if(BigDecimalUtil.lt(info.getMoney(), renewOrder.getAmount())) {
            // 余额不足
            return Result.error("用户余额不足");
        }
        UserInfo sInfo = new UserInfo();
        sInfo.setId(info.getId());
        sInfo.setMoney(BigDecimalUtil.subtract(info.getMoney(), renewOrder.getAmount()));
        boolean infoFlag = infoService.updateById(sInfo);
        if(!infoFlag) return Result.error("支付失败，请稍后再试");
        // 更新订单数据
        orderService.payRenewOfOrder(renewOrder);
        return Result.success("续借成功");
    }

    /**
     * 预约归还
     * @param order
     * @return
     */
    @PutMapping("/status/repay")
    public Result<String> repayOfOrder(@RequestBody Order order) {
        boolean flag = orderService.repayOfOrder(order.getId(), order.getReturnTime());
        if(!flag) return Result.error("预约失败");
        return Result.success("预约成功");
    }

    /**
     * 评价
     * @param order
     * @return
     */
    @PutMapping("/comment")
    public Result<String> commentOfOrder(@RequestBody Order order) {
        return Result.success("评论成功");
    }

    /**
     * 申请售后
     * @param order
     * @return
     */
    @PutMapping("/status/aAS")
    public Result<String> applyAfterSalesOfOrder(@RequestBody Order order) {
        boolean flag = orderService.updateOrderStatus(order.getId(), 8);
        if(!flag) return Result.error("售后申请失败");
        return Result.success("售后申请成功");
    }

    /**
     * 申请退款
     * @param order
     * @return
     */
    @PutMapping("/status/refund")
    public Result<String> applyRefundOfOrder(@RequestBody Order order) {
        // 设置订单的售后状态为 1
        boolean flag = orderService.updateOrderAmStatus(order.getId(), 1);
        if(!flag) return Result.error("退款申请失败");
        return Result.success("退款申请成功");
    }
}
