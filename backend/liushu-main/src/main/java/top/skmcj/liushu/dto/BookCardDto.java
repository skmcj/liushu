package top.skmcj.liushu.dto;

import lombok.Data;
import top.skmcj.liushu.entity.Book;

/**
 * 图书卡片信息
 */
@Data
public class BookCardDto extends Book {

    /**
     * 图书简介
     */
    private String outline;

    /**
     * 图书所属书店名称
     */
    private String storeName;
}
