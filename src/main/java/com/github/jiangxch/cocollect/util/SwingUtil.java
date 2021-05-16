package com.github.jiangxch.cocollect.util;

import com.github.jiangxch.cocollect.dao.CategoryDao;
import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

/**
 * @author: jiangxch
 * @date: 2021/5/14 上午2:31
 */
public class SwingUtil {
    private static Timer tipsTimer = new Timer("tipsTimer");

    public static void showTips(JLabel tipLabel,String tipText,Integer ms) {
        String oldText = tipLabel.getText();
        Color oldForeground = tipLabel.getForeground();
        tipLabel.setForeground(JBColor.RED);
        tipLabel.setText(tipText);
        tipsTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                tipLabel.setText(oldText);
                tipLabel.setForeground(oldForeground);
            }
        },ms);
    }

    public static void showTipsForNotNull(JLabel itemLabel) {
        showTips(itemLabel,"该项必填哦",5000);
    }

    /*
     * 打开选择文件窗口
     */
    public static void showFileSelectWindow(String defaultShowCategoryPath,Component parent, Function<File,Object> function) {
        // 创建一个默认的文件选取器
        JFileChooser fileChooser = new JFileChooser();

        // 设置默认显示的文件夹为当前文件夹
        File defaultShowCategoryPathFile = new File(defaultShowCategoryPath);
        if (!defaultShowCategoryPathFile.exists()) {
            defaultShowCategoryPathFile = new File(".");
        }
        fileChooser.setCurrentDirectory(defaultShowCategoryPathFile);

        // 设置文件选择的模式（只选文件、只选文件夹、文件和文件均可选）
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // 设置是否允许多选
        fileChooser.setMultiSelectionEnabled(false);

        // 添加可用的文件过滤器（FileNameExtensionFilter 的第一个参数是描述, 后面是需要过滤的文件扩展名 可变参数）
        //fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("zip(*.zip, *.rar)", "zip", "rar"));
        // 设置默认使用的文件过滤器
        //fileChooser.setFileFilter(new FileNameExtensionFilter("image(*.jpg, *.png, *.gif)", "jpg", "png", "gif"));

        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"确定", 则获取选择的文件路径
            File file = fileChooser.getSelectedFile();
            function.apply(file);
            // 如果允许选择多个文件, 则通过下面方法获取选择的所有文件
            // File[] files = fileChooser.getSelectedFiles();
        }
    }

}
