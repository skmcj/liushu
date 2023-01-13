package top.skmcj.liushu.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.skmcj.liushu.entity.*;

@Data
@AllArgsConstructor
public class CommentIdEntityBo {
    /**
     * 用户信息实体
     */
    private UserInfo info;

    /**
     * 商家信息实体
     */
    private Bookstore store;

    /**
     * 图书信息实体
     */
    private Book book;

    /**
     * 订单信息实体
     */
    private Order order;

    /**
     * 订单项信息实体
     */
    private OrderItem item;
}
