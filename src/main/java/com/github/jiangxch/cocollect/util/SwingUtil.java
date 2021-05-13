package com.github.jiangxch.cocollect.util;

import com.intellij.ui.JBColor;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

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

}
