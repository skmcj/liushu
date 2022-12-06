package top.skmcj.liushu.dto;

import lombok.Data;
import top.skmcj.liushu.entity.Cart;

@Data
public class CartDto extends Cart {

    /**
     * 所属书店名
     */
    private String storeName;

    /**
     * 图书名
     */
    private String bookName;

    /**
     * 图书封面
     */
    private String bookCover;

    /**
     * 图书简介
     */
    private String bookOutline;

    /**
     * 图书库存
     */
    private Integer inventory;
}
