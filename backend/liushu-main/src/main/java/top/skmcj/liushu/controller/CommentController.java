package top.skmcj.liushu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.skmcj.liushu.common.Result;
import top.skmcj.liushu.dto.CommentDto;
import top.skmcj.liushu.entity.*;
import top.skmcj.liushu.service.CommentService;
import top.skmcj.liushu.service.OrderItemService;
import top.skmcj.liushu.service.OrderService;
import top.skmcj.liushu.service.ReplyService;
import top.skmcj.liushu.util.CommonUtil;
import top.skmcj.liushu.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论接口
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService itemService;

    /**
     * 添加评论
     * @param comment
     * @return
     */
    @Transactional
    @PostMapping
    public Result<String> addCommentByUser(@RequestBody Comment comment, HttpServletRequest request) throws Exception {
        Order order = orderService.getById(comment.getOrderId());
        if(order == null) return Result.error("查无对应订单");
        if(order.getStatus() < 4) return Result.error("订单尚不可评论");
        if(comment.getOrderId() == null) return Result.error("评价订单ID不可为空");
        if(comment.getOrderItemId() == null) return Result.error("评价订单项ID不可为空");
        if(comment.getBookId() == null) return Result.error("评价图书ID不可为空");
        if(comment.getStoreId() == null) return Result.error("评价商家ID不可为空");

        String token = request.getHeader("Authorization");
        User user = JwtUtil.verifyTokenOfUser(token);
        Comment sComment = new Comment();
        sComment.setOrderId(comment.getOrderId());
        sComment.setOrderItemId(comment.getOrderItemId());
        sComment.setBookId(comment.getBookId());
        sComment.setUserId(user.getId());
        sComment.setStoreId(comment.getStoreId());
        sComment.setContent(comment.getContent());
        sComment.setScore(comment.getScore());
        sComment.setIsAnonymous(comment.getIsAnonymous());
        boolean flag = commentService.save(sComment);
        if(!flag) return Result.error("添加评论失败");
        // 删除成功，将评论开关关闭
        // 订单评论
        if(order.getIsComment().equals(0)) {
            Order sOrder = new Order();
            sOrder.setId(order.getId());
            sOrder.setIsComment(1);
            orderService.updateById(sOrder);
        }
        // 订单项评论
        OrderItem orderItem = new OrderItem();
        orderItem.setId(sComment.getOrderItemId());
        orderItem.setIsComment(1);
        itemService.updateById(orderItem);
        return Result.success("添加评论成功");
    }

    /**
     * 修改评论
     * @param comment
     * @return
     */
    @PutMapping
    public Result<String> updateCommentByUser(@RequestBody Comment comment, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        User user = JwtUtil.verifyTokenOfUser(token);
        Comment oComment = commentService.getById(comment.getId());
        if(oComment == null) return Result.error("没有相关评论");
        if(!oComment.getUserId().equals(user.getId())) return Result.error("不可修改他人的评论");
        Comment uComment = new Comment();
        uComment.setId(comment.getId());
        uComment.setContent(comment.getContent());
        uComment.setScore(comment.getScore());
        uComment.setIsAnonymous(comment.getIsAnonymous());
        boolean flag = commentService.updateById(uComment);
        if(!flag) return Result.error("修改评论失败");
        return Result.success("修改评论成功");
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    @Transactional
    @DeleteMapping
    public Result<String> deleteCommentByUser(Long id, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        User user = JwtUtil.verifyTokenOfUser(token);
        Comment oComment = commentService.getById(id);
        if(oComment == null) return Result.error("没有相关评论");
        if(!oComment.getUserId().equals(user.getId())) return Result.error("不可删除他人的评论");
        // 删除
        boolean flag = commentService.removeById(id);
        if(!flag) return Result.error("评论删除失败");
        // 删除成功，将评论开关打开
        OrderItem orderItem = new OrderItem();
        orderItem.setId(oComment.getOrderItemId());
        orderItem.setIsComment(0);
        itemService.updateById(orderItem);
        return Result.success("评论删除成功");
    }

    /**
     * 根据评论ID获取评论
     * @param id
     * @return
     */
    @GetMapping("/id/{id}")
    public Result<CommentDto> getCommentById(@PathVariable Long id, HttpServletRequest request) {
        String imgDoMain = CommonUtil.getImgDoMain(request);
        CommentDto commentDto = commentService.getCommentById(id, imgDoMain);
        if(commentDto == null) return Result.error("查无相应评论");
        return Result.success(commentDto);
    }

    /**
     * 分页获取用户评论
     * @param currentPage
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/user")
    public Result<Page<CommentDto>> getCommentPageByUser(int currentPage, int pageSize, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        User user = JwtUtil.verifyTokenOfUser(token);
        String imgDoMain = CommonUtil.getImgDoMain(request);
        Page<CommentDto> commentDtoPage = commentService.getCommentPageByUser(user.getId(), currentPage, pageSize,
                imgDoMain);
        // 封装存入订单号
        // 订单字典
        Map<Long, Order> orderMap = new HashMap<>();
        List<CommentDto> commentDtos = commentDtoPage.getRecords();
        commentDtos.stream().forEach(item -> {
            if(orderMap.containsKey(item.getOrderId())) {
                // 字典存有订单信息
                Order order = orderMap.get(item.getOrderId());
                item.setOrderNumber(order.getNumber());
            } else {
                Order order = orderService.getById(item.getOrderId());
                orderMap.put(item.getOrderId(), order);
                item.setOrderNumber(order.getNumber());
            }
        });
        return Result.success(commentDtoPage);
    }

    /**
     * 分页获取商家评论
     * @param currentPage
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/store")
    public Result<Page<CommentDto>> getCommentPageByStore(int currentPage, int pageSize, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        Employee employee = JwtUtil.verifyTokenOfEmployee(token);
        String imgDoMain = CommonUtil.getImgDoMain(request);
        Page<CommentDto> commentDtoPage = commentService.getCommentPageByStore(employee.getStoreId(), currentPage
                , pageSize, imgDoMain);
        // 封装存入订单号
        // 订单字典
        Map<Long, Order> orderMap = new HashMap<>();
        List<CommentDto> commentDtos = commentDtoPage.getRecords();
        commentDtos.stream().forEach(item -> {
            if(orderMap.containsKey(item.getOrderId())) {
                // 字典存有订单信息
                Order order = orderMap.get(item.getOrderId());
                item.setOrderNumber(order.getNumber());
            } else {
                Order order = orderService.getById(item.getOrderId());
                orderMap.put(item.getOrderId(), order);
                item.setOrderNumber(order.getNumber());
            }
        });
        return Result.success(commentDtoPage);
    }

    /**
     * 分页获取图书评论
     * @param bookId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/book")
    public Result<Page<CommentDto>> getCommentPageByBook(Long bookId, int currentPage, int pageSize,
                                                         HttpServletRequest request) {
        String imgDoMain = CommonUtil.getImgDoMain(request);
        Page<CommentDto> commentDtoPage = commentService.getCommentPageByBook(bookId, currentPage, pageSize,
                imgDoMain);
        return Result.success(commentDtoPage);
    }

    /**
     * 根据订单ID获取用户订单相关评论
     * @param orderId
     * @param request
     * @return
     */
    @GetMapping("/user/order")
    public Result<List<CommentDto>> getCommentListByOrderOfUser(Long orderId, HttpServletRequest request) throws Exception {
        Order order = orderService.getById(orderId);
        if(order == null) return Result.error("查无相关订单");
        String token = request.getHeader("Authorization");
        User user = JwtUtil.verifyTokenOfUser(token);
        if(!order.getUserId().equals(user.getId())) return Result.error("订单ID与登录信息不匹配");
        String imgDoMain = CommonUtil.getImgDoMain(request);
        List<CommentDto> commentDtos = commentService.getCommentListByOrder(orderId, order.getUserId(),
                order.getStoreId(), imgDoMain);
        return Result.success(commentDtos);
    }

    /**
     * 根据订单ID获取商家订单相关评论
     * @param orderId
     * @param request
     * @return
     */
    @GetMapping("/store/order")
    public Result<List<CommentDto>> getCommentListByOrderOfStore(Long orderId, HttpServletRequest request) throws Exception {
        Order order = orderService.getById(orderId);
        if(order == null) return Result.error("查无相关订单");
        String token = request.getHeader("Authorization");
        Employee employee = JwtUtil.verifyTokenOfEmployee(token);
        if(!order.getStoreId().equals(employee.getStoreId())) return Result.error("订单ID与登录信息不匹配");
        String imgDoMain = CommonUtil.getImgDoMain(request);
        List<CommentDto> commentDtos = commentService.getCommentListByOrder(orderId, order.getUserId(),
                order.getStoreId(), imgDoMain);
        return Result.success(commentDtos);
    }

    /**
     * 根据订单项ID获取订单评论
     * @param orderItemId
     * @return
     */
    @GetMapping("/orderItem")
    public Result<CommentDto> getCommentByOrderItem(Long orderItemId, HttpServletRequest request) {
        String imgDoMain = CommonUtil.getImgDoMain(request);
        CommentDto commentDto = commentService.getCommentByOrderItem(orderItemId, imgDoMain);
        if(commentDto == null) return Result.error("用户尚未评论");
        return Result.success(commentDto);
    }

    /**
     * 用户回复【评论】
     * @param reply
     * @return
     */
    @PostMapping("/user/reply")
    public Result<String> addReplyByUserOfReply(@RequestBody Reply reply, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        User user = JwtUtil.verifyTokenOfUser(token);
        Reply sReply = new Reply();
        sReply.setCommentId(reply.getCommentId());
        sReply.setUserId(user.getId());
        sReply.setContent(reply.getContent());
        boolean flag = replyService.save(sReply);
        if(!flag) return Result.error("回复失败");
        return Result.success("回复成功");
    }

    /**
     * 商家回复【评论】
     * @param reply
     * @return
     */
    @PostMapping("/store/reply")
    public Result<String> addReplyByStoreOfComment(@RequestBody Reply reply, HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        Employee employee = JwtUtil.verifyTokenOfEmployee(token);
        Reply sReply = new Reply();
        sReply.setCommentId(reply.getCommentId());
        sReply.setStoreId(employee.getStoreId());
        sReply.setContent(reply.getContent());
        boolean flag = replyService.save(sReply);
        if(!flag) return Result.error("回复失败");
        return Result.success("回复成功");
    }
}
