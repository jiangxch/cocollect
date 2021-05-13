package com.github.jiangxch.cocollect.dao;

import com.github.jiangxch.cocollect.dao.impl.file.FileCategoryDaoImpl;
import com.github.jiangxch.cocollect.entity.CategoryEntity;

import java.util.List;

/**
 * @author: jiangxch
 * @date: 2021/5/13 下午8:56
 */
public interface CategoryDao {
    void insert(String categoryName,String pid);
    void updateById(String categoryName,String categoryId);
    void deleteById(String categoryId);
    List<CategoryEntity> listAll();

    static CategoryDao getIns() {
        return new FileCategoryDaoImpl();
    }
}
