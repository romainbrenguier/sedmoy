package com.github.romainbrenguier.plugin;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import org.jetbrains.annotations.NotNull;

public class NotificationAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        // Setup handler
        EditorActionManager actionManager = EditorActionManager.getInstance();
        TypedAction typedAction = actionManager.getTypedAction();
        typedAction.setupHandler(new EditHandler());

        // Notification
        final NotificationGroup notificationGroup = new NotificationGroup("Sedmoy",
                NotificationDisplayType.BALLOON,
                true);
        notificationGroup.createNotification("Start sedmoy edit handler",
                "Execute an action on each edit",
                NotificationType.INFORMATION, null)
                .notify(e.getProject());
    }
}
