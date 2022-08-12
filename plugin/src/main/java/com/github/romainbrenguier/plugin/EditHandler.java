package com.github.romainbrenguier.plugin;

import com.github.romainbrenguier.sedmoy.app.FormulaTableEvaluator;
import com.github.romainbrenguier.sedmoy.app.GroovyException;
import com.github.romainbrenguier.sedmoy.app.GroovyInterpreter;
import com.github.romainbrenguier.sedmoy.model.CsvParser;
import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Dimension;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
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
import java.util.Collections;
import java.util.function.Supplier;
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
        if (!name.endsWith(".csv") && !name.endsWith(".groovy"))
            return;

        final Project project = editor.getProject();
        final JTable tableComponent = SedmoyToolWindowFactory.getTableComponent(project);
        final String text = document.getText();
        Supplier<DataTable> dataTableSupplier;
        if (name.endsWith("csv")) {
            dataTableSupplier = () ->
                    new CsvParser().parseLines(Arrays.stream(text.split("\n"))
                            .collect(Collectors.toList()));
        } else if (name.endsWith(".groovy")) {
            dataTableSupplier = () -> {
                final GroovyInterpreter groovyInterpreter = new GroovyInterpreter();
                final FormulaTable formula = new FormulaTable(new Dimension(1, 1), text);
                try {
                    return new FormulaTableEvaluator().evaluate(groovyInterpreter, Collections.emptyMap(), formula);
                } catch (GroovyException e) {
                    return new DataTable(Collections.singletonList(Collections.singletonList(e.getMessage())));
                }
            };
        } else {
            throw new AssertionError("Unhandled file extension " + name);
        }
        Runnable action = () -> {
            tableComponent.setModel(DataTableModel.nonEditable(dataTableSupplier.get()));
        };
        WriteCommandAction.runWriteCommandAction(project, action);
    }
}
