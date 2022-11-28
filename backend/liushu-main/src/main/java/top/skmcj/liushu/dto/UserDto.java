package top.skmcj.liushu.dto;

import lombok.Data;
import top.skmcj.liushu.entity.User;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UserDto extends User {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别，0-未知；1-男；2-女
     */
    private Integer sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 账户零钱余额
     */
    private BigDecimal money;

    /**
     * 验证码
     */
    private String code;

    /**
     * token
     */
    private String token;
}
