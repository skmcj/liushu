package top.skmcj.liushu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地址表
 */
@Data
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 地址id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收货人姓名
     */
    private String consignee;

    /**
     * 性别称呼，0-先生；1-女士
     */
    private Integer sex;

    /**
     * 区号，+86(默认，中国大陆)，+866(中国台湾)，+852(中国香港)，+853(中国澳门)
     */
    private String areaCode;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 地址定位
     */
    private String location;

    /**
     * 详细地址
     */
    private String detail;

    /**
     * 标签，家、公司、学校等
     */
    private String label;

    /**
     * 是否默认，0-否；1-是
     */
    private Integer isDefault;

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
