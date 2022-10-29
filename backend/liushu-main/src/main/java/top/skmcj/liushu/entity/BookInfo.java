package top.skmcj.liushu.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 图书版权信息表
 */
@Data
public class BookInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版权信息id
     */
    private Long id;

    /**
     * 所属图书id
     */
    private Long bookId;

    /**
     * 图书ISBN
     */
    private String isbn;

    /**
     * 条形码
     */
    private String barCode;

    /**
     * 装帧
     */
    private String binding;

    /**
     * 版次
     */
    private Integer version;

    /**
     * 册数
     */
    private Integer volume;

    /**
     * 重量
     */
    private Integer weight;

    /**
     * 印刷次数
     */
    private Integer printTimes;

}

