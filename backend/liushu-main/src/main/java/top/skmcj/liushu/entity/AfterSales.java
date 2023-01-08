package top.skmcj.liushu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 售后单据表
 */
@Data
public class AfterSales implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 售后单ID
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单售后时状态
     */
    private Integer orderStatus;

    /**
     * 售后类型：0-退款；1-退货退款；2-换货(暂不设计实现)
     */
    private Integer type;

    /**
     * 售后状态：0-审核中；1-待退货；2-待回收；3-待退款；4-已退货；5-已退款；6-待换货；7-售后已结束
     */
    private Integer status;

    /**
     * 售后原因：退款原因、换货原因等
     */
    private String reason;

    /**
     * 售后失败原因
     */
    private String failReason;

    /**
     * 售后补充凭证(图片信息)
     */
    private String proof;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 审核通过时间
     */
    private LocalDateTime agreeTime;

    /**
     * 退货模式：0-7天期限；2-双倍期限；3-无期限
     * (7天未默认配置，可在项目文档中配置)
     */
    private Integer returnMode;

    /**
     * 退货预约时间
     */
    private LocalDateTime returnTime;

    /**
     * 退货回收时间
     */
    private LocalDateTime recycleTime;

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
