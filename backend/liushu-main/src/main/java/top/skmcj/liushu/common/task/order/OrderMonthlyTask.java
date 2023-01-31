package top.skmcj.liushu.common.task.order;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.skmcj.liushu.entity.*;
import top.skmcj.liushu.service.*;
import top.skmcj.liushu.util.BigDecimalUtil;
import top.skmcj.liushu.util.TimeUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单每月任务
 */
@Slf4j
@Component
public class OrderMonthlyTask {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService itemService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookstoreService storeService;

    @Autowired
    private CommentService commentService;

    /**
     * 处理每月订单
     */
    public void handleOrderMonthly() {
        // 获取上一月份所有订单
        List<Order> orders = this.getOrderListByLastMonth();
        // 统计订单借阅量
        // 图书借阅量
        Map<Long, Integer> bookMap = this.countOrderMbaOfBook(orders);
        this.saveBookMap(bookMap);
        // 商家借阅量
        Map<Long, Integer> storeMap = this.countOrderMbaOfStore(orders);
        this.saveStoreMap(storeMap);
        // 获取上月图书评论
        List<Comment> comments = this.getCommentListOfLastMonth();
        // 处理评分
        this.handleCommentScore(comments);
    }

    /**
     * 根据月份获取订单
     * @return
     */
    private List<Order> getOrderListByLastMonth() {
        LocalDateTime start = TimeUtil.lastMonthStartTime();
        LocalDateTime end = TimeUtil.lastMonthEndTime();
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.ge(Order::getCreateTime, start);
        orderWrapper.le(Order::getCreateTime, end);
        List<Order> orders = orderService.list(orderWrapper);
        return orders;
    }

    /**
     * 计算图书月借阅量
     * @param orders
     * @return
     */
    private Map<Long, Integer> countOrderMbaOfBook(List<Order> orders) {
        Map<Long, Integer> map = new HashMap<>();
        for(Order order : orders) {
            LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
            itemWrapper.eq(OrderItem::getOrderId, order.getId());
            List<OrderItem> orderItems = itemService.list(itemWrapper);
            for(OrderItem item : orderItems) {
                if(map.containsKey(item.getBookId())) {
                    Integer old = map.get(item.getBookId());
                    map.remove(item.getBookId());
                    map.put(item.getBookId(), old + 1);
                } else {
                    map.put(item.getBookId(), 1);
                }
            }
        }
        return map;
    }

    /**
     * 计算商家月借阅量
     * @param orders
     * @return
     */
    private Map<Long, Integer> countOrderMbaOfStore(List<Order> orders) {
        Map<Long, Integer> map = new HashMap<>();
        for(Order order : orders) {
            if(map.containsKey(order.getStoreId())) {
                Integer old = map.get(order.getStoreId());
                map.remove(order.getStoreId());
                map.put(order.getStoreId(), old + 1);
            } else {
                map.put(order.getStoreId(), 1);
            }
        }
        return map;
    }

    /**
     * 保存图书借阅数据
     * @param bookMap
     */
    private void saveBookMap(Map<Long, Integer> bookMap) {
        for (Long bookId : bookMap.keySet()) {
            Book oldBook = bookService.getById(bookId);
            Book book = new Book();
            book.setId(bookId);
            book.setMba(bookMap.get(bookId));
            book.setTba(oldBook.getTba() + bookMap.get(bookId));
            bookService.updateById(book);
        }
    }

    /**
     * 保存商家借阅数据
     * @param storeMap
     */
    private void saveStoreMap(Map<Long, Integer> storeMap) {
        for (Long storeId : storeMap.keySet()) {
            Bookstore store = new Bookstore();
            store.setId(storeId);
            store.setMba(storeMap.get(storeId));
            storeService.updateById(store);
        }
    }

    /**
     * 获取上个月的订单项评论
     * @return
     */
    private List<Comment> getCommentListOfLastMonth() {
        LocalDateTime start = TimeUtil.lastMonthStartTime();
        LocalDateTime end = TimeUtil.lastMonthEndTime();
        LambdaQueryWrapper<Comment> commentWrapper = new LambdaQueryWrapper<>();
        commentWrapper.ge(Comment::getCreateTime, start);
        commentWrapper.le(Comment::getCreateTime, end);
        List<Comment> comments = commentService.list(commentWrapper);
        return comments;
    }

    /**
     * 处理评论评分
     * @param comments
     */
    private void handleCommentScore(List<Comment> comments) {
        // 图书评分
        Map<Long, BigDecimal> bookScoreMap = new HashMap<>();
        Map<Long, Integer> bookCountMap = new HashMap<>();
        // 商家评分
        Map<Long, BigDecimal> storeScoreMap = new HashMap<>();
        Map<Long, Integer> storeCountMap = new HashMap<>();
        for(Comment comment : comments) {
            // 计算图书评分
            if(bookScoreMap.containsKey(comment.getBookId())) {
                BigDecimal tScore = BigDecimalUtil.add(bookScoreMap.get(comment.getBookId()), comment.getScore());
                Integer oldCount = bookCountMap.get(comment.getBookId());
                bookScoreMap.remove(comment.getBookId());
                bookCountMap.remove(comment.getBookId());
                bookScoreMap.put(comment.getBookId(), tScore);
                bookCountMap.put(comment.getBookId(), oldCount + 1);
            } else {
                bookScoreMap.put(comment.getBookId(), comment.getScore());
                bookCountMap.put(comment.getBookId(), 1);
            }
            // 计算商家评分
            if(storeScoreMap.containsKey(comment.getStoreId())) {
                BigDecimal tScore = BigDecimalUtil.add(storeScoreMap.get(comment.getStoreId()), comment.getScore());
                Integer oldCount = storeCountMap.get(comment.getStoreId());
                storeScoreMap.remove(comment.getStoreId());
                storeCountMap.remove(comment.getStoreId());
                storeScoreMap.put(comment.getStoreId(), tScore);
                storeCountMap.put(comment.getStoreId(), oldCount + 1);
            } else {
                storeScoreMap.put(comment.getStoreId(), comment.getScore());
                storeCountMap.put(comment.getStoreId(), 1);
            }
        }
        // 保存图书评分
        for(Long bookId : bookScoreMap.keySet()) {
            BigDecimal score = BigDecimalUtil.divideScore(bookScoreMap.get(bookId), bookCountMap.get(bookId));
            Book book = bookService.getById(bookId);
            Book sBook = new Book();
            sBook.setId(bookId);
            if(book.getScore() == null) {
                sBook.setScore(score);
            } else {
                BigDecimal incScore = BigDecimalUtil.add(book.getScore(), score);
                BigDecimal newScore = BigDecimalUtil.divideScore(incScore, 2);
                sBook.setScore(newScore);
            }
            // System.out.println("图书评分 ==> " + bookId + " : " + sBook.getScore());
            bookService.updateById(sBook);
        }
        // 保存商家评分
        for(Long storeId : storeScoreMap.keySet()) {
            BigDecimal score = BigDecimalUtil.divideScore(storeScoreMap.get(storeId), storeCountMap.get(storeId));
            Bookstore store = storeService.getById(storeId);
            Bookstore bookstore = new Bookstore();
            bookstore.setId(storeId);
            if(store.getScore() == null) {
                bookstore.setScore(score);
            } else {
                BigDecimal incScore = BigDecimalUtil.add(store.getScore(), score);
                BigDecimal newScore = BigDecimalUtil.divideScore(incScore, 2);
                bookstore.setScore(newScore);
            }
            // System.out.println("商家评分 ==> " + storeId + " : " + bookstore.getScore());
            storeService.updateById(bookstore);
        }
    }

}
