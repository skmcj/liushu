package top.skmcj.liushu.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接收图书分页请求数据
 */
@Data
public class BookPageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 图书名称
     */
    private String name;

    /**
     * 图书作者
     */
    private String author;

    /**
     * 图书出版社
     */
    private String press;

    /**
     * 图书类别ID
     */
    private Long bookCateId;

    /**
     * 图书店内分类ID
     */
    private Long goodsCateId;

}
