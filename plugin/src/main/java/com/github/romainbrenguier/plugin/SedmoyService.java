package com.github.romainbrenguier.plugin;

import com.github.romainbrenguier.plugin.EditHandler.EvaluationResult;
import com.github.romainbrenguier.sedmoy.app.FormulaTableEvaluator;
import com.github.romainbrenguier.sedmoy.app.GroovyException;
import com.github.romainbrenguier.sedmoy.app.GroovyInterpreter;
import com.github.romainbrenguier.sedmoy.model.CsvParser;
import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Dimension;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import com.github.romainbrenguier.sedmoy.ui.DataTableModel;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

@Service
public final class SedmoyService {

  @Nullable
  Document currentDocument;

  public void setCurrentDocument(Document document) {
    currentDocument = document;
  }

  public static SedmoyService getInstance() {
    return ApplicationManager.getApplication()
        .getService(SedmoyService.class);
  }

  public void setStatus(Project project, String text) {
    SedmoyToolWindowFactory.getStatusComponent(project).setText(text);
  }

  public void updateToolWindow(Project project) {
    if (currentDocument == null) {
      setStatus(project, "No selected document");
      return;
    }
    final VirtualFile virtualFile = FileDocumentManager.getInstance()
        .getFile(currentDocument);
    final String name = virtualFile.getName();
    if (!name.endsWith(".csv") && !name.endsWith(".groovy"))
      return;

    final String text = currentDocument.getText();
    Supplier<EvaluationResult> dataTableSupplier;
    if (name.endsWith("csv")) {
      dataTableSupplier = () ->
          new EvaluationResult(
              new CsvParser().parseLines(Arrays.stream(text.split("\n"))
                  .collect(Collectors.toList())),
              "Csv file loaded");
    } else if (name.endsWith(".groovy")) {
      dataTableSupplier = () -> {
        final Path directory = Paths.get(virtualFile.getPath()).getParent();
        System.out.println("Directory : " + directory);
        GroovyInterpreter groovyInterpreter;
        try {
          groovyInterpreter = new GroovyInterpreter(directory);
        } catch (MalformedURLException e) {
          e.printStackTrace();
          groovyInterpreter = new GroovyInterpreter();
        }
        final FormulaTable formula = new FormulaTable(new Dimension(1, 1), text);
        try {
          final DataTable table = new FormulaTableEvaluator()
              .evaluateCollector(groovyInterpreter, formula);
          return new EvaluationResult(
              table, "Groovy script OK");
        } catch (GroovyException e) {
          System.out.println("Compilation failed:");
          e.printStackTrace();
          return new EvaluationResult(
              null,
              "Groovy error: " + e.getMessage());
        }
      };
    } else {
      throw new AssertionError("Unhandled file extension " + name);
    }
    Runnable action = () -> {
      final EvaluationResult result = dataTableSupplier.get();
      SedmoyToolWindowFactory.getStatusComponent(project).setText(
          result.statusText);
      if (result.dataTable != null) {
        SedmoyToolWindowFactory.getTableComponent(project)
            .setModel(DataTableModel.nonEditable(result.dataTable));
      }
    };
    WriteCommandAction.runWriteCommandAction(project, action);
  }
}
