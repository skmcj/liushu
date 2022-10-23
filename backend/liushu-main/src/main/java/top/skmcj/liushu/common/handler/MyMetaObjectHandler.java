package top.skmcj.liushu.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static top.skmcj.liushu.common.enums.ZoneEnum.*;

/**
 * mybatis-plus 公共字段处理器
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 处理insert操作的公共字段
     * 即实体类中带有@TableField(fill = FieldFill.INSERT)注解的成员
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now(ZoneId.of(SHANGHAI.getZone())));
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now(ZoneId.of(SHANGHAI.getZone())));
    }

    /**
     * 处理update操作的公共字段
     * 即实体类中带有@TableField(fill = FieldFill.UPDATE)注解的成员
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now(ZoneId.of(SHANGHAI.getZone())));
    }
}
