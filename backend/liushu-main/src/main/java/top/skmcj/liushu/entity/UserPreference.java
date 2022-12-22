package top.skmcj.liushu.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户喜好度表
 */
@Data
public class UserPreference implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 喜好记录ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 图书ID
     */
    private Long bookId;

    /**
     * 喜好度
     */
    private Float score;

    /**
     * 时间
     */
    private LocalDateTime time;

}
