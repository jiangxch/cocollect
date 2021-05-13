package com.github.jiangxch.cocollect.util;

import java.util.UUID;

/**
 * @author: jiangxch
 * @date: 2021/5/13 下午8:47
 */
public class IdUtil {
    public static String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
