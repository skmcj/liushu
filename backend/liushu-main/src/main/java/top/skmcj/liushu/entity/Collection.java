package top.skmcj.liushu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收藏表
 */
@Data
public class Collection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，收藏记录ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商家ID
     */
    private Long storeId;

    /**
     * 商家名称
     */
    private String storeName;

    /**
     * 商家门脸图片
     */
    private String storeCover;

    /**
     * 商家评分
     */
    private BigDecimal score;

    /**
     * 商家标签
     */
    private String label;

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

}
