package com.oylong.myfund.window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.oylong.myfund.data.FundData;
import org.jetbrains.annotations.NotNull;

/**
 * window工厂类
 *
 * @author OyLong
 * @date 2021/02/22 22:12
 **/
public class FundWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        FundWindow fundWindow = new FundWindow(project, toolWindow);
        FundData fundData = new FundData();
        fundData.getDwjz();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(fundWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
