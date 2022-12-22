package top.skmcj.liushu.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import top.skmcj.liushu.entity.Book;
import top.skmcj.liushu.entity.BookCost;
import top.skmcj.liushu.entity.BookDetail;
import top.skmcj.liushu.entity.BookInfo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 图书完整信息
 */
@Data
public class BookDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 图书基本信息
     */
    private Book book;

    /**
     * 图书版权信息
     */
    private BookInfo bookInfo;

    /**
     * 图书详情信息
     */
    private BookDetail bookDetail;

    /**
     * 图书费用信息
     */
    private BookCost bookCost;

    /**
     * 其它信息
     */

    /**
     * 图书类别名称
     */
    private String bookCateName;
}
