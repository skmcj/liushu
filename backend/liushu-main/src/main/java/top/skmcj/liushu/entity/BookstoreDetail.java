package top.skmcj.liushu.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
     * 营业执照Url
     */
    @TableField(exist = false)
    private String businessLicenseUrl;

    /**
     * 出版物经营许可证
     */
    private String licenseImg;

    /**
     * 出版物经营许可证Url
     */
    @TableField(exist = false)
    private String licenseImgUrl;

    /**
     * 店内环境
     */
    private String envImgs;

    /**
     * 店内环境Url
     */
    @TableField(exist = false)
    private String envImgsUrl;

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
     * 获取营业执照列表
     * @return
     */
    public List<String> getBusinessLicenseList() {
        return JSON.parseObject(businessLicense, List.class);
    }

    /**
     * 获取出版物经营许可证列表
     * @return
     */
    public List<String> getLicenseImgList() {
        return JSON.parseObject(licenseImg, List.class);
    }

    /**
     * 获取店内环境列表
     * @return
     */
    public List<String> getEnvImgsList() {
        return JSON.parseObject(envImgs, List.class);
    }

    /**
     * 获取营业执照Url列表
     * @return
     */
    public List<String> getBusinessLicenseUrl() {
        return JSON.parseObject(businessLicenseUrl, List.class);
    }

    /**
     * 获取出版物经营许可证Url列表
     * @return
     */
    public List<String> getLicenseImgUrl() {
        return JSON.parseObject(licenseImgUrl, List.class);
    }

    /**
     * 获取店内环境Url列表
     * @return
     */
    public List<String> getEnvImgsUrl() {
        return JSON.parseObject(envImgsUrl, List.class);
    }
}
