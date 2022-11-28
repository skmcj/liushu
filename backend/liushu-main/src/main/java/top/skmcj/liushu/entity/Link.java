package top.skmcj.liushu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 友情链接表
 */
@Data
public class Link implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，链接ID
     */
    private Long id;

    /**
     * 网站名称
     */
    private String name;

    /**
     * 网站链接
     */
    private String link;

    /**
     * 网站Logo
     */
    private String logo;

    /**
     * 联系邮箱
     */
    private String email;

    /**
     * 网站简介
     */
    private String profile;

    /**
     * 审核标志，0-待审核；1-审核通过；2-审核失败
     */
    private Integer flag;

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
