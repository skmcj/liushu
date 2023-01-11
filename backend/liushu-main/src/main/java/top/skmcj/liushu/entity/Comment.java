package top.skmcj.liushu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 评论表
 */
@Data
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论id
     */
    private Long id;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单详情id
     */
    private Long orderItemId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 图书id
     */
    private Long bookId;

    /**
     * 书店id
     */
    private Long storeId;

    /**
     * 书店名
     */
    @TableField(exist = false)
    private String storeName;

    /**
     * 图书名称
     */
    @TableField(exist = false)
    private String bookName;

    /**
     * 图书封面
     */
    @TableField(exist = false)
    private String bookCover;

    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private String nickname;

    /**
     * 用户头像
     */
    @TableField(exist = false)
    private String userAvatar;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评分
     */
    private BigDecimal score;

    /**
     * 是否匿名，0-不匿；1-匿名
     */
    private Integer isAnonymous;

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
     * 是否删除，0-默认；1-删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;

}
