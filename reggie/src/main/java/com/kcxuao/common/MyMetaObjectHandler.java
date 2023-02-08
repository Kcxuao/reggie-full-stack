package com.kcxuao.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.kcxuao.Utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自动填充
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("自动填充【 新增 】");

        Long id = Long.valueOf((String) RedisUtils.get("id"));

        try {
            metaObject.setValue("updateTime", LocalDateTime.now());
            metaObject.setValue("createTime", LocalDateTime.now());

            metaObject.setValue("createUser", id);
            metaObject.setValue("updateUser", id);
        } catch (ReflectionException ex) {
            metaObject.setValue("createTime", LocalDateTime.now());
            log.error(ex.getMessage());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("自动填充【 修改 】");

        Long id = Long.valueOf((String) RedisUtils.get("id"));

        metaObject.setValue("updateUser", id);
        metaObject.setValue("updateTime", LocalDateTime.now());
    }
}
