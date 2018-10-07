package com.warpfuture.util;

import org.springframework.ui.Model;

import java.util.Map;

public class ModelBeanUtil {

    /**
     * 从Model中拿到需要的对象
     *
     * @param model Model
     * @param key key
     * @param clazz Class
     * @param <T> T
     * @return T
     */
    public static <T> T getBeanFromModel(Model model, String key, Class<T> clazz) {
        Map<String, Object> map = model.asMap();
        Object obj = map.get(key);
        T t = null;
        if (clazz.isInstance(obj)) {
            t = clazz.cast(obj);
        }
        return t;
    }

}
