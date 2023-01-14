package top.skmcj.liushu.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 常见文件ContentType枚举
 */
@Getter
@AllArgsConstructor
public enum FileContentTypeEnum {

    JPG("jpg", "image/jpeg"),
    TIFF("tiff", "image/tiff"),
    GIF("gif", "image/gif"),
    JFIF("jfif", "image/jpeg"),
    PNG("png", "image/png"),
    SVG("svg", "image/svg+xml"),
    TIF("tif", "image/tiff"),
    ICO("ico", "image/x-icon"),
    JPEG("jpeg", "image/jpeg"),
    WBMP("wbmp", "image/vnd.wap.wbmp"),
    FAX("fax", "image/fax"),
    NET("net", "image/pnetvue"),
    JPE("jpe", "image/jpeg"),
    RP("rp", "image/vnd.rn-realpix");

    /**
     * 图片类型名
     */
    private String typeName;
    /**
     * 对应的ContentType
     */
    private String contentType;

    /**
     * 获取指定文件类型的ContentType
     * 没有则返回 null
     * @return
     */
    public static String getTypeOfContentType(String type) {
        for (ImageContentTypeEnum imageEnum : ImageContentTypeEnum.values()) {
            if(imageEnum.getTypeName().equals(type)) {
                return imageEnum.getContentType();
            }
        }
        return null;
    }
}
