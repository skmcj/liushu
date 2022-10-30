package top.skmcj.liushu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单表
 */
@Data
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private Long id;

    /**
     * 订单号
     */
    private String number;

    /**
     * 所属用户id
     */
    private Long userId;

    /**
     * 所属书店id
     */
    private Long storeId;

    /**
     * 订单地址id
     */
    private Long addressId;

    /**
     * 收货人姓名
     */
    private String consignee;

    /**
     * 收货人联系方式
     */
    private String phone;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 借阅时长(天)
     */
    private Integer borrowTime;

    /**
     * 可续借时长，默认为图书设定值
     */
    private Integer renewDuration;

    /**
     * 期望配送时间
     */
    private LocalDateTime expectedTime;

    /**
     * 配送费
     */
    private BigDecimal deliveryFee;

    /**
     * 配送方式，0-由商家配送；1-由平台配送
     */
    private Integer shippingMethod;

    /**
     * 支付方式，0-在线支付
     */
    private Integer payMethod;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 订单总金额
     */
    private BigDecimal orderAmount;

    /**
     * 优惠金额，默认为0
     */
    private BigDecimal discountAmount;

    /**
     * 实付金额 = 订单总金额 - 优惠金额
     */
    private BigDecimal amount;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 交易状态，0-进行中；1-已完成；2-已取消
     */
    private Integer tradeStatus;

    /**
     * 支付状态，0-未支付，1-已支付
     */
    private Integer payStatus;

    /**
     * 售后状态，0-待售后；1-待退款；2-已退款；3-待退货；4-已退货
     */
    private Integer amStatus;

    /**
     * 订单状态，0-待配送；1-待收货；2-待归还；3-待上门；4-待评价；5-已完成；6-逾期中；7-已逾期；8-售后中
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
     * 是否删除，0-默认；1-删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;

}
