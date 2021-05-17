package com.github.jiangxch.cocollect.util;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

/**
 * @author: jiangxch
 * @date: 2021/5/13 下午8:52
 */
public class SystemUtil {
    public static String getUserDir() {
        return System.getProperty("user.home");
    }

    public static void copyText2Clipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
    }
}
