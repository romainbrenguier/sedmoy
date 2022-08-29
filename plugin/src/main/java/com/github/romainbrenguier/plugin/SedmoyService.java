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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.swing.JTable;
import javax.swing.JTextArea;

@Service
public final class SedmoyService {

  @Nullable
  Document currentDocument;

  private Map<Project, JTextArea> statusText = new HashMap<>();
  private Map<Project, JTable> tableComponents = new HashMap<>();

  public static SedmoyService getInstance() {
    return ApplicationManager.getApplication()
        .getService(SedmoyService.class);
  }

  public void setCurrentDocument(Document document) {
    currentDocument = document;
  }

  public void registerStatusTextArea(Project project, JTextArea textArea) {
    statusText.put(project, textArea);
  }

  public void registerTableComponent(Project project, JTable table) {
    tableComponents.put(project, table);
  }

  public void setStatus(Project project, String text) {
    statusText.get(project).setText(text);
  }

  public void updateToolWindow(Project project) {
    if (currentDocument == null) {
      setStatus(project, "No selected document");
      return;
    }
    final VirtualFile virtualFile = FileDocumentManager.getInstance()
        .getFile(currentDocument);
    final String name = virtualFile.getName();
    if (!name.endsWith(".csv") && !name.endsWith(".groovy")) {
      return;
    }

    final String text = currentDocument.getText();
    Supplier<EvaluationResult> dataTableSupplier;
    if (name.endsWith("csv")) {
      dataTableSupplier = () ->
          new EvaluationResult(
              new CsvParser().parseLines(Arrays.stream(text.split("\n"))
                  .collect(Collectors.toList())),
              "Csv file loaded");
    } else if (name.endsWith(".groovy")) {
      dataTableSupplier = () -> evaluateGroovy(virtualFile, text);
    } else {
      throw new AssertionError("Unhandled file extension " + name);
    }
    Runnable action = () -> {
      final EvaluationResult result = dataTableSupplier.get();
      setStatus(project, result.statusText);
      if (result.dataTable != null) {
        tableComponents.get(project)
            .setModel(DataTableModel.nonEditable(result.dataTable));
      }
    };
    WriteCommandAction.runWriteCommandAction(project, action);
  }

  EvaluationResult evaluateGroovy(VirtualFile virtualFile, String text) {
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
      final StringWriter stringWriter = new StringWriter();
      e.printStackTrace(new PrintWriter(stringWriter));
      return new EvaluationResult(
          null,
          "Groovy error: " + e.getMessage() + "\n"
              + stringWriter.toString());
    }
  }
}