package com.github.romainbrenguier.plugin;

import com.github.romainbrenguier.sedmoy.model.CsvParser;
import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.ui.DataTableModel;
import com.intellij.codeInsight.template.impl.editorActions.TypedActionHandlerBase;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;
import java.util.stream.Collectors;

class EditHandler extends TypedActionHandlerBase {

    EditHandler(TypedActionHandler original) {
        super(original);
    }

    @Override
    public void execute(@NotNull Editor editor,
                        char c,
                        @NotNull DataContext dataContext) {
        myOriginalHandler.execute(editor, c, dataContext);
        updateToolWindow(editor);
    }

    public static void updateToolWindow(@NotNull Editor editor) {
        Document document = editor.getDocument();
        final String name = FileDocumentManager.getInstance().getFile(document).getName();
        if (name.endsWith("csv")) {
            final Project project = editor.getProject();
            final JTable tableComponent = SedmoyToolWindowFactory.getTableComponent(project);
            final String text = document.getText();
            Runnable runnable = () ->
            {
                final DataTable dataTable =
                        new CsvParser().parseLines(Arrays.stream(text.split("\n")).collect(Collectors.toList()));
                tableComponent.setModel(DataTableModel.nonEditable(dataTable));
            };
            WriteCommandAction.runWriteCommandAction(project, runnable);
        }
    }
}
