package top.skmcj.liushu.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用于接收商家审核资料相关信息
 */
@Data
public class BusinessAuthVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 书店名称
     */
    private String storeName;

    /**
     * 门脸图片
     */
    private String cover;

    /**
     * 店铺地址
     */
    private String address;

    /**
     * 营业时间
     */
    private String businessHours;

    /**
     * 配送服务，0-由商家配送
     */
    private Integer distribution;

    /**
     * 配送费
     */
    private BigDecimal deliverFee;

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
}
