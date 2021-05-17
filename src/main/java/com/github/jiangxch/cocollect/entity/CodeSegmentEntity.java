package com.github.jiangxch.cocollect.entity;

import java.io.Serializable;

/**
 * @author: jiangxch
 * @date: 2021/5/13 下午9:01
 */
public class CodeSegmentEntity implements Serializable {
    private String id;
    /**
     * 不能为null
     */
    private String categoryId;
    private String name;
    private String code;

    @Override
    public String toString() {
        String treeNodeName = name;
        if (!code.contains(" ")) {// 方法签名或者类全名
            treeNodeName += "-j";
        } else {
            treeNodeName += "-c";
        }
        return treeNodeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
