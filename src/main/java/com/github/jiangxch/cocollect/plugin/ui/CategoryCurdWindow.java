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
public class CategoryCurdWindow extends BaseWindow{

    private JPanel root;
    private JTextField categoryNameTextField;
    private JButton confirmButton;
    private JLabel categoryNameLabel;
    private JLabel parentCategoryLabel;
    private JComboBox<CategoryEntity> parentCategoryComboBox;

    private JFrame jframe;


    private CategoryEntity updateCategoryEntity;
    private CocollectToolWindow cocollectToolWindow;

    public CategoryCurdWindow(CocollectToolWindow cocollectToolWindow) {
        this.cocollectToolWindow = cocollectToolWindow;
        initUi();
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
                CategoryDao.getIns().updateById(text,updateCategoryEntity.getId());
            } else {
                CategoryDao.getIns().insert(text,selectedItem.getId());
            }
            jframe.setVisible(false);
            cocollectToolWindow.refreshTree();
        });
    }



    public void showInsertWindow(CategoryEntity parentCategoryEntity) {
        refreshCategoryComboBox(parentCategoryComboBox,parentCategoryEntity.getId());
        categoryNameTextField.setText(null);
        updateCategoryEntity = null;
        jframe.setVisible(true);
    }

    public void showUpdateWindow(CategoryEntity updateCategoryEntity) {
        refreshCategoryComboBox(parentCategoryComboBox,updateCategoryEntity.getPid());
        this.updateCategoryEntity = updateCategoryEntity;
        categoryNameTextField.setText(updateCategoryEntity.getName());
        jframe.setVisible(true);
    }
}
