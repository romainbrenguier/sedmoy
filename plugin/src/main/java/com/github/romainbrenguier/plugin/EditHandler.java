package com.github.romainbrenguier.plugin;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

class EditHandler implements TypedActionHandler {
    @Override
    public void execute(@NotNull Editor editor,
                        char c,
                        @NotNull DataContext dataContext) {
        Document document = editor.getDocument();
        Project project = editor.getProject();
        final String name = FileDocumentManager.getInstance().getFile(document).getName();
        if (name.endsWith("csv")) {
            final String text = document.getText();
            final JComponent component = ToolWindowManager.getInstance(project).getToolWindow("Sedmoy").getComponent();
            Runnable runnable = () -> document.insertString(0, "sedmoy active\n");
            WriteCommandAction.runWriteCommandAction(project, runnable);
        }
    }
}
