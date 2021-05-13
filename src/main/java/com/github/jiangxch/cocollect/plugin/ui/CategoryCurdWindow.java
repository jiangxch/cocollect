package com.github.jiangxch.cocollect.plugin.ui;

import com.github.jiangxch.cocollect.constant.GlobalConstant;
import com.github.jiangxch.cocollect.dao.CategoryDao;
import com.github.jiangxch.cocollect.entity.CategoryEntity;
import com.github.jiangxch.cocollect.util.SwingUtil;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: jiangxch
 * @date: 2021/5/13 下午11:52
 */
public class CategoryCurdWindow {
    private static CategoryCurdWindow INS = new CategoryCurdWindow();

    private static final int WIDTH = 700;
    private static final int HEIGHT = 800;

    private JPanel root;
    private JTextField categoryNameTextField;
    private JButton confirmButton;
    private JLabel categoryNameLabel;
    private JLabel parentCategoryLabel;
    private JComboBox<CategoryEntity> parentCategoryComboBox;

    private JFrame jframe;


    private CategoryEntity updateCategoryEntity;

    private CategoryCurdWindow() {
        initUi();
    }

    public static CategoryCurdWindow getIns() {
        return INS;
    }

    private void initUi() {
        initButton();

        jframe = new JFrame("目录");
        jframe.setContentPane(root);
        jframe.setSize(500, 500);
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jframe.setVisible(false);
    }

    private void initButton() {
        confirmButton.addActionListener(e -> {
            String text = categoryNameTextField.getText();
            CategoryEntity selectedItem = (CategoryEntity) parentCategoryComboBox.getSelectedItem();
            if (text == null || text.length() == 0) {
                SwingUtil.showTipsForNotNull(categoryNameLabel);
                return;
            }
            if (selectedItem == null) {
                SwingUtil.showTipsForNotNull(parentCategoryLabel);
                return;
            }
            if (updateCategoryEntity != null) {
                CategoryDao.getIns().updateById(updateCategoryEntity.getId(),text);
            } else {
                CategoryDao.getIns().insert(text,selectedItem.getId());
            }
        });
    }

    private Map<String, CategoryEntity> idCategoryEntityMap = new HashMap<>();

    private void refreshCategoryComboBox() {
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
    }

    public void showInsertWindow() {
        refreshCategoryComboBox();
        categoryNameTextField.setText(null);
        updateCategoryEntity = null;
        jframe.setVisible(true);
    }

    public void showUpdateWindow(CategoryEntity selectCategoryEntity) {
        refreshCategoryComboBox();
        updateCategoryEntity = selectCategoryEntity;
        parentCategoryComboBox.setSelectedItem(selectCategoryEntity.getPid());
        categoryNameTextField.setText(null);
        jframe.setVisible(true);
    }
}
