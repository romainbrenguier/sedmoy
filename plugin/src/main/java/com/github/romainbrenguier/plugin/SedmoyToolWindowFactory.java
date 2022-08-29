package com.github.romainbrenguier.plugin;

import com.github.romainbrenguier.sedmoy.ui.EmptyTableModel;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SedmoyToolWindowFactory implements ToolWindowFactory {

  public static final String SEDMOY_TOOL_WINDOW = "sedmoyToolWindow";

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

  @Override
  public void createToolWindowContent(@NotNull Project project,
      @NotNull ToolWindow toolWindow) {
    final JPanel toolPanel = new JPanel(new GridLayout(0, 1));
    final JTextArea statusText = new JTextArea("Loading...");
    final SedmoyService sedmoyService = SedmoyService.getInstance();
    sedmoyService.registerStatusTextArea(project, statusText);
    toolPanel.add(new JScrollPane(statusText));
    final JTable tableComponent = new JTable(new EmptyTableModel());
    sedmoyService.registerTableComponent(project, tableComponent);
    toolPanel.add(tableComponent);
    final JButton updateButton = new JButton("Update");
    toolPanel.add(updateButton);
    ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
    Content content = contentFactory
        .createContent(toolPanel, SEDMOY_TOOL_WINDOW, false);
    toolWindow.getContentManager().addContent(content);
    setupEditHandler(project);
    updateButton.addActionListener(actionEvent ->
        sedmoyService.updateToolWindow(project));
  }
}
