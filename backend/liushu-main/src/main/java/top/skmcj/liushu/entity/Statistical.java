package top.skmcj.liushu.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 表记录统计表
 */
@Data
public class Statistical implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键
     */
    private Integer id;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表的记录数
     */
    private Long record;

}
