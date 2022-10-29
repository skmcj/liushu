package top.skmcj.liushu.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 图书详情表
 */
@Data
public class BookDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 图书详情id
     */
    private Long id;

    /**
     * 所属图书id
     */
    private Long bookId;

    /**
     * 本书特色
     */
    private String featureDesc;

    /**
     * 图书简介
     */
    private String outline;

    /**
     * 作者简介
     */
    private String authorDesc;

}
