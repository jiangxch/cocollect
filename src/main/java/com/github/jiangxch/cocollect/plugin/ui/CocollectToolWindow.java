package com.github.jiangxch.cocollect.plugin.ui;

import javax.swing.*;
import java.awt.*;

/**
 * @author: jiangxch
 * @date: 2021/5/13 下午9:49
 */
public class CocollectToolWindow {
    private static CocollectToolWindow INS = new CocollectToolWindow();

    private static final int WIDTH = 700;
    private static final int HEIGHT = 800;

    private JPanel root;
    private JButton addCategoryButton;
    private JButton updateCategoryButton;
    private JButton deleteCategoryButton;
    private JButton addCodeSegmentButton;
    private JButton updateCodeSegmentButton;
    private JButton updateCocollectConfigButton;
    private JTree categoryCodeTree;
    private JLabel tipsLabel;


    private JFrame jframe;

    public static CocollectToolWindow getIns() {
        return INS;
    }

    public CocollectToolWindow() {
        init();
    }

    private void init() {
        initButton();
        refreshTree();
        jframe = new JFrame("cocollect");
        jframe.pack();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(false);
    }

    private void refreshTree() {

    }

    private void initButton() {
        addCategoryButton.addActionListener(e -> {
            getSelectUserObject();
            CategoryCurdWindow.getIns().showInsertWindow();
        });
    }

    private Object getSelectUserObject() {

        return null;
    }

    public JPanel getRoot() {
        return root;
    }
}
