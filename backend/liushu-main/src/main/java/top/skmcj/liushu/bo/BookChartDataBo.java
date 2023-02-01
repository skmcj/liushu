package top.skmcj.liushu.bo;

import lombok.Data;

import java.util.List;

@Data
public class BookChartDataBo {

    /**
     * 最小值
     */
    private Integer min;

    /**
     * 最大值
     */
    private Integer max;

    /**
     * 图书数据
     */
    private List<List> data;
}
