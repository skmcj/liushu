package top.skmcj.liushu.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 书店信息表
 */
@Data
public class Bookstore implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 书店id、im_id
     */
    private Long id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 店长密码
     */
    private String password;

    /**
     * 书店名称
     */
    private String storeName;

    /**
     * 门脸图片
     */
    private String cover;

    /**
     * 门脸图片URL
     */
    @TableField(exist = false)
    private String coverUrl;

    /**
     * 评分
     */
    private BigDecimal score;

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
    private BigDecimal deliveryFee;

    /**
     * 默认首次借阅时长
     */
    private Integer borrowDay;

    /**
     * 默认可续借时长
     */
    private Integer renewDay;

    /**
     * 商家服务
     */
    private String storeService;

    /**
     * 标签
     */
    private String label;

    /**
     * 公告
     */
    private String notice;

    /**
     * 收入
     */
    private BigDecimal income;

    /**
     * 店铺状态。0-休息；1-营业
     */
    private Integer status;

    /**
     * 审核状态，0-未提交；1-审核中；2-通过；3-不通过；4-中途审核
     */
    private Integer auditStatus;

    /**
     * 月借阅量
     */
    private Integer mba;

    /**
     * 店铺前缀
     */
    private String prefix;

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
     * 获取标签列表
     * @return
     */
    public List<String> getLabelList() {
        return JSON.parseObject(label, List.class);
    }

    /**
     * 配送服务
     * @return
     */
    public String getDistributionStr() {
        if(distribution == null) return null;
        if(distribution == 0) {
            return "由 商家 配送";
        }
        return "由 平台 配送";
    }
}
