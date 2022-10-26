package top.skmcj.liushu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 图书表
 */
@Data
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 图书id
     */
    private Long id;

    /**
     * 所属书店id
     */
    private Long storeId;

    /**
     * 封面
     */
    private String cover;

    /**
     * 图书名称
     */
    private String name;

    /**
     * 作者
     */
    private String author;

    /**
     * 图书所属类别
     */
    private Long bookCateId;

    /**
     * 所属店内分类
     */
    private Long goodsCateId;

    /**
     * 出版社
     */
    private String press;

    /**
     * 出版时间
     */
    private LocalDateTime pubDate;

    /**
     * 开本
     */
    private String size;

    /**
     * 页数
     */
    private Integer pages;

    /**
     * 库存
     */
    private Integer inventory;

    /**
     * 售卖状态，0-禁售；1-启售
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建者
     */
    private Long createUser;

    /**
     * 更新者
     */
    private Long updateUser;

    /**
     * 是否删除，0-默认；1-删除
     */
    private Integer isDeleted;

}
