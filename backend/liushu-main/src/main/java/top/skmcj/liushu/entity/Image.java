package top.skmcj.liushu.entity;

import lombok.Data;

@Data
public class Image {
    // 图片名
    private String name;
    // 图片大小
    private Long size;
    // 图片链接
    private String url;
    // 图片URI
    private String uri;
    // 图片类型
    private String type;
}
