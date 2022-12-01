package top.skmcj.liushu.vo;

import lombok.Data;

@Data
public class LinkVo {
    /**
     * 主键，链接ID
     */
    private Long id;

    /**
     * 网站名称
     */
    private String name;

    /**
     * 网站链接
     */
    private String link;

    /**
     * 网站Logo
     */
    private String logo;

    /**
     * 联系邮箱
     */
    private String email;

    /**
     * 链接类型
     */
    private String type;

    /**
     * 网站简介
     */
    private String profile;

    /**
     * 审核标志，0-待审核；1-审核通过；2-审核失败
     */
    private Integer flag;
}
