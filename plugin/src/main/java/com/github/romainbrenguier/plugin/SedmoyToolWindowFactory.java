package com.github.romainbrenguier.plugin;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import com.github.romainbrenguier.sedmoy.ui.EmptyTableModel;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class SedmoyToolWindowFactory implements ToolWindowFactory {

    public static final String SEDMOY_TOOL_WINDOW = "sedmoyToolWindow";

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        final JPanel toolPanel = new JPanel(new FlowLayout());
        final JTable tableComponent = new JTable(new EmptyTableModel());
        toolPanel.add(tableComponent);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(toolPanel, SEDMOY_TOOL_WINDOW, false);
        toolWindow.getContentManager().addContent(content);
        setupEditHandler(project);
    }

    private static void setupEditHandler(@Nullable Project project) {
        // Setup handler
        TypedAction typedAction = TypedAction.getInstance();
        typedAction.setupHandler(new EditHandler(typedAction.getHandler()));

        // Notification
        final NotificationGroup notificationGroup = new NotificationGroup("Sedmoy",
                NotificationDisplayType.BALLOON,
                true);
        notificationGroup.createNotification("Start sedmoy edit handler",
                        "Execute an action on each edit",
                        NotificationType.INFORMATION, null)
                .notify(project);
    }

    public static JTable getTableComponent(Project project) {
        final Component component = ToolWindowManager.getInstance(project).getToolWindow(
                        "Sedmoy")
                .getContentManager().findContent(SedmoyToolWindowFactory.SEDMOY_TOOL_WINDOW).getComponent()
                .getComponent(0);
        return (JTable) component;
    }
}
