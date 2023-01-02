package top.skmcj.liushu.dto;

import lombok.Data;
import top.skmcj.liushu.entity.Order;
import top.skmcj.liushu.entity.OrderItem;

import java.util.List;

/**
 * 订单完整数据
 */
@Data
public class OrderDto extends Order {

    /**
     * 商家名称
     */
    private String storeName;

    /**
     * 订单项列表
     */
    private List<OrderItem> orderItems;
}
