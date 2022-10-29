package top.skmcj.liushu.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 图书类别表
 */
@Data
public class BookCate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类别id
     */
    private Integer id;

    /**
     * 类别名称
     */
    private String name;

    /**
     * 排序依据，权值
     */
    private Integer sort;

}
