package com.github.romainbrenguier.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class SedmoyToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        final JPanel toolPanel = new JPanel(new FlowLayout());
        final JButton saveButton = new JButton("Save");
        toolPanel.add(saveButton);
        // saveButton.addActionListener(actionEvent -> save(frame));

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(toolPanel, "", false);
        toolWindow.getContentManager().addContent(content);

    }
}
