package com.github.romainbrenguier.plugin;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

class EditHandler implements TypedActionHandler {
    @Override
    public void execute(@NotNull Editor editor,
                        char c,
                        @NotNull DataContext dataContext) {
        Document document = editor.getDocument();
        Project project = editor.getProject();
        Runnable runnable = () -> document.insertString(0, "sedmoy active\n");
        WriteCommandAction.runWriteCommandAction(project, runnable);
    }
}
