package top.skmcj.liushu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 书店详情表
 */
@Data
public class BookstoreDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 书店详情id
     */
    private Long id;

    /**
     * 书店id
     */
    private Long storeId;

    /**
     * 法人姓名
     */
    private String ownerName;

    /**
     * 法人身份证号
     */
    private String idNumber;

    /**
     * 法人手机号
     */
    private String phone;

    /**
     * 营业执照
     */
    private String businessLicense;

    /**
     * 出版物经营许可证
     */
    private String licenseImg;

    /**
     * 店内环境
     */
    private String envImgs;

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
