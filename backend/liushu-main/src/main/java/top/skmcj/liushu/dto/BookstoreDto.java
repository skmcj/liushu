package top.skmcj.liushu.dto;

import lombok.Data;
import top.skmcj.liushu.entity.Bookstore;
import top.skmcj.liushu.entity.BookstoreDetail;

/**
 * 商家完整信息
 */
@Data
public class BookstoreDto {

    /**
     * 商家基本信息
     */
    private Bookstore bookstore;

    /**
     * 商家详细信息
     */
    private BookstoreDetail bookstoreDetail;
}
