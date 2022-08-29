package com.github.romainbrenguier.plugin;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.intellij.codeInsight.template.impl.editorActions.TypedActionHandlerBase;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import javax.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

class EditHandler extends TypedActionHandlerBase {

  EditHandler(TypedActionHandler original) {
    super(original);
  }

  public static void updateToolWindow(@NotNull Editor editor) {
    final SedmoyService sedmoyService = SedmoyService.getInstance();
    sedmoyService.setCurrentDocument(editor.getDocument());
    sedmoyService.updateToolWindow(editor.getProject());
  }

  @Override
  public void execute(@NotNull Editor editor,
      char c,
      @NotNull DataContext dataContext) {
    myOriginalHandler.execute(editor, c, dataContext);
    updateToolWindow(editor);
  }

  static class EvaluationResult {

    @Nullable
    final DataTable dataTable;
    final String statusText;

    EvaluationResult(DataTable dataTable, String statusText) {
      this.dataTable = dataTable;
      this.statusText = statusText;
    }
  }
}
