package com.github.jiangxch.cocollect.plugin.ui;

import com.github.jiangxch.cocollect.constant.GlobalConstant;
import com.github.jiangxch.cocollect.dao.CategoryDao;
import com.github.jiangxch.cocollect.entity.CategoryEntity;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 目录窗口和代码片段窗口都需要目录的筛选器,编写鸡肋,服用目录下拉筛选代码
 *
 * @author: jiangxch
 * @date: 2021/5/15 上午4:06
 */
public class BaseWindow {
    protected static Map<String, CategoryEntity> idCategoryEntityMap = new HashMap<>();
    protected static void refreshCategoryComboBox(JComboBox<CategoryEntity> parentCategoryComboBox,String selectedCategoryId) {
        idCategoryEntityMap.clear();
        parentCategoryComboBox.removeAllItems();
        List<CategoryEntity> categoryEntities = CategoryDao.getIns().listAll();
        CategoryEntity root = new CategoryEntity();
        root.setId(GlobalConstant.CATEGORY_ROOT_ID);
        root.setName(GlobalConstant.CATEGORY_ROOT_NAME);
        categoryEntities.add(root);
        idCategoryEntityMap.put(GlobalConstant.CATEGORY_ROOT_ID, root);
        for (CategoryEntity categoryEntity : categoryEntities) {
            parentCategoryComboBox.addItem(categoryEntity);
            idCategoryEntityMap.put(categoryEntity.getId(), categoryEntity);
        }
        parentCategoryComboBox.setSelectedItem(idCategoryEntityMap.getOrDefault(selectedCategoryId,root));
    }
}
