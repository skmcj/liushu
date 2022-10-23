package top.skmcj.liushu.vo;

import lombok.Data;

/**
 * 用于接收审核商家
 */
@Data
public class ExamineBusinessVo {
    // 商家ID
    private Long storeId;
    // 管理员用户名
    private String username;
    // 密码
    private String password;
    /**
     * 审核结果
     * 0-失败；1-成功
     */
    private Integer result;
}
