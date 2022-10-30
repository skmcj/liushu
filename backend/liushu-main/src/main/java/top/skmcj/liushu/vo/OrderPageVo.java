package top.skmcj.liushu.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接收订单分页参数
 */
@Data
public class OrderPageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 订单号
     */
    private String number;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 联系人
     */
    private String consignee;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

}
