package com.github.jiangxch.cocollect.plugin;

import com.github.jiangxch.cocollect.plugin.ui.CocollectToolWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author: jiangxch
 * @date: 2021/4/29 上午12:58
 */
public class CocollectToolWindowFactory implements ToolWindowFactory {
    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        CocollectToolWindow ins = CocollectToolWindow.getIns();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(ins.getRoot(), "Cocollect", false);
        toolWindow.getContentManager().addContent(content);
    }

}
