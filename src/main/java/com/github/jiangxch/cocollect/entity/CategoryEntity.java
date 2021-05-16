package com.github.jiangxch.cocollect.entity;

import java.io.Serializable;

/**
 * @author: jiangxch
 * @date: 2021/5/13 下午9:00
 */
public class CategoryEntity implements Serializable {
    private String id;
    /**
     * 不能为null
     */
    private String pid;
    private String name;

    @Override
    public String toString() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
