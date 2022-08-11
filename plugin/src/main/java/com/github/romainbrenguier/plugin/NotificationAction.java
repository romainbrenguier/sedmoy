package com.github.romainbrenguier.plugin;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NotificationAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        setupEditHandler(e.getProject());
    }

    public static void setupEditHandler(@Nullable Project project) {
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
}
