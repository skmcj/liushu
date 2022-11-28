package top.skmcj.liushu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 回复表
 */
@Data
public class Reply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 回复id
     */
    private Long id;

    /**
     * 父评论id
     */
    private Long commentId;

    /**
     * 书店id
     */
    private Long storeId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 回复内容
     */
    private String content;

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
