package com.github.jiangxch.cocollect.plugin.ui;

import com.github.jiangxch.cocollect.dao.CodeSegmentDao;
import com.github.jiangxch.cocollect.entity.CategoryEntity;
import com.github.jiangxch.cocollect.entity.CodeSegmentEntity;
import com.github.jiangxch.cocollect.util.SwingUtil;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author: jiangxch
 * @date: 2021/5/15 上午3:57
 */
public class CodeSegmentCurdWindow extends BaseWindow {

    private JPanel root;
    private JComboBox<CategoryEntity> categoryComboBox;
    private JTextField codeSegmentNameTextField;
    private JTextArea codeTextArea;
    private JButton confirmButton;
    private JLabel categoryLabel;
    private JLabel codeSegmentNameLabel;
    private JLabel codeLabel;
    private JScrollPane codeTextAreaJscrollPane;


    private JFrame jframe;

    private CodeSegmentEntity updateCodeSegmentEntity;
    private CocollectToolWindow cocollectToolWindow;

    public CodeSegmentCurdWindow(CocollectToolWindow cocollectToolWindow) {
        this.cocollectToolWindow = cocollectToolWindow;
        initUi();
    }

    private void initUi() {
        initButton();
        initCodeTextArea();
        initListen();

        jframe = new JFrame("代码片段");
        jframe.setContentPane(root);
        jframe.setSize(600, 800);
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jframe.setVisible(false);
    }

    private void initListen() {
        codeSegmentNameTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (13 == keyCode) {// 回车
                    confirm();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private void initCodeTextArea() {
        codeTextAreaJscrollPane.setViewportView(codeTextArea);
        codeTextAreaJscrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        // 是否自动换行
        codeTextArea.setLineWrap(true);
    }

    public void showCreateCodeSegmentWindow(CategoryEntity categoryEntity) {
        this.updateCodeSegmentEntity = null;
        fillUiData(categoryEntity.getId(),null,null);
        jframe.setVisible(true);
    }

    public void showCreateCodeSegmentWindowForQuickInsert(String text) {
        this.updateCodeSegmentEntity = null;
        fillUiData(null,null,text);
        jframe.setVisible(true);
    }

    public void showUpdateCodeSegmentWindow(CodeSegmentEntity updateCodeSegmentEntity) {
        this.updateCodeSegmentEntity = updateCodeSegmentEntity;
        fillUiData(updateCodeSegmentEntity.getCategoryId(),updateCodeSegmentEntity.getName(),updateCodeSegmentEntity.getCode());
        jframe.setVisible(true);
    }

    private void fillUiData(String categoryId, String name, String code) {
        refreshCategoryComboBox(categoryComboBox, categoryId);
        codeSegmentNameTextField.setText(name);
        codeTextArea.setText(code);
    }

    private void initButton() {
        confirmButton.addActionListener(e -> {
            confirm();
        });
    }

    private void confirm() {
        CategoryEntity selectedCategoryEntity = (CategoryEntity) categoryComboBox.getSelectedItem();
        if (selectedCategoryEntity == null || selectedCategoryEntity.getId() == null) {
            SwingUtil.showTipsForNotNull(categoryLabel);
            return;
        }
        String codeSegmentCategoryId = selectedCategoryEntity.getId();

        String codeSegmentName = codeSegmentNameTextField.getText();
        if (codeSegmentName == null) {
            SwingUtil.showTipsForNotNull(codeSegmentNameLabel);
            return;
        }
        String code = codeTextArea.getText();
        if (code == null) {
            SwingUtil.showTipsForNotNull(codeLabel);
            return;
        }
        if (updateCodeSegmentEntity == null) {
            CodeSegmentEntity codeSegmentEntity = new CodeSegmentEntity();
            codeSegmentEntity.setCategoryId(codeSegmentCategoryId);
            codeSegmentEntity.setName(codeSegmentName);
            codeSegmentEntity.setCode(code);

            CodeSegmentDao.getIns().insert(codeSegmentEntity);
        } else {
            CodeSegmentEntity codeSegmentEntity = new CodeSegmentEntity();
            codeSegmentEntity.setCategoryId(codeSegmentCategoryId);
            codeSegmentEntity.setName(codeSegmentName);
            codeSegmentEntity.setCode(code);
            CodeSegmentDao.getIns().updateById(codeSegmentEntity, updateCodeSegmentEntity.getId());
        }
        jframe.setVisible(false);
        cocollectToolWindow.refreshTree();
    }
}
