package com.github.jiangxch.cocollect.plugin.action;

import com.github.jiangxch.cocollect.plugin.ui.CocollectToolWindow;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;

/**
 * @author: jiangxch
 * @date: 2021/5/17 下午9:27
 */
public class QuickInsertCodeSegmentAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (editor == null)
            return;
        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();
        CocollectToolWindow.getIns().getCodeSegmentCurdWindow().showCreateCodeSegmentWindowForQuickInsert(selectedText);
    }
}
