package top.skmcj.liushu.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 时区枚举
 */
@Getter
@AllArgsConstructor
public enum ZoneEnum {
    SHANGHAI("Asia/Shanghai", "中国上海"),
    HONGKONG("Asia/Hong_Kong", "中国香港"),
    MACAO("Asia/Macao", "中国澳门"),
    TAIPEI("Asia/Taipei", "中国台湾(台北)"),
    TOKYO("Asia/Tokyo", "日本东京"),
    SEOUL("Asia/Seoul", "韩国首尔"),
    SINGAPORE("Asia/Singapore", "新加坡"),
    MOSCOW("Europe/Moscow", "俄罗斯莫斯科"),
    LONDON("Europe/London", "英国伦敦"),
    PARIS("Europe/Paris", "法国巴黎"),
    ROME("Europe/Rome", "意大利罗马"),
    NEWYORK("America/New_York", "美国纽约");

    // 时区
    private String zone;
    // 时区描述
    private String desc;
}
