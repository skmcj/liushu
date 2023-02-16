package top.skmcj.liushu.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargeBo {

    /**
     * 卡号
     */
    private String cardNumber;

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 支付密码
     */
    private String payPass;
}
