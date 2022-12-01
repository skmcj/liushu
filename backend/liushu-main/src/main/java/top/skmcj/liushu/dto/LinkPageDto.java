package top.skmcj.liushu.dto;

import lombok.Data;

import java.util.List;

@Data
public class LinkPageDto<T> {
    /**
     * 总数
     */
    private Integer total;

    /**
     * 页数据
     */
    private List<T> rows;

    /**
     * 链接类型
     */
    private String type;

    /**
     * 一些说明
     */
    private String info;
}
