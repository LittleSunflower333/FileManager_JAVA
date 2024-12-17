package com.fileManager.handler;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

public class MyMetaObjectHandler implements MetaObjectHandler {

    // 插入时自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        // 填充 createdAt 和 updatedAt 字段
        this.setFieldValByName("createdAt", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updatedAt", LocalDateTime.now(), metaObject);
        this.setFieldValByName("uploadedAt", LocalDateTime.now(), metaObject);
    }

    // 更新时自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时只填充 updatedAt 字段
        this.setFieldValByName("updatedAt", LocalDateTime.now(), metaObject);

    }
}
