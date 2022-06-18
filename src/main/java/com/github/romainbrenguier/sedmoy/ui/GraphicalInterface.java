package com.github.romainbrenguier.sedmoy.ui;

import com.github.romainbrenguier.sedmoy.app.GroovyInterpreter;
import com.github.romainbrenguier.sedmoy.app.TableEvaluator;
import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Dimension;
import com.github.romainbrenguier.sedmoy.model.Document;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import com.github.romainbrenguier.sedmoy.model.Table;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

public class GraphicalInterface {

  private final Document document;

  private final TableEvaluator tableEvaluator = new TableEvaluator(
      new GroovyInterpreter());
  private JPanel mainPanel;
  private GraphicalComponents graphicalComponents = new GraphicalComponents();

  public GraphicalInterface(Document document) {
    this.document = document;
  }

  public void run() {
    final JFrame frame = new JFrame("Sedmoy");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());

    final JPanel toolPanel = new JPanel(new FlowLayout());

    final JButton saveButton = new JButton("Save");
    toolPanel.add(saveButton);
    saveButton.addActionListener(actionEvent -> save());

    final JButton evaluateButton = new JButton("Evaluate");
    evaluateButton.addActionListener(actionEvent -> evaluate());
    toolPanel.add(evaluateButton);

    final JButton addFormulaButton = new JButton("Add formula");
    addFormulaButton.addActionListener(actionEvent -> addFormula());
    toolPanel.add(addFormulaButton);

    frame.add(BorderLayout.NORTH, toolPanel);

    mainPanel = new JPanel();
    graphicalComponents.initialize(mainPanel, document);
    fillDocumentComponents(document, tableEvaluator.evaluate(document));

    frame.add(BorderLayout.CENTER, mainPanel);
    frame.setLocationRelativeTo(null);
    frame.pack();
    frame.setVisible(true);
  }

  private void addFormula() {
    mainPanel.setVisible(false);
    document.add(freshTableName(),
        new FormulaTable(new Dimension(10, 1), "0"));
    graphicalComponents.initialize(mainPanel, document);
    fillDocumentComponents(document, tableEvaluator.evaluate(document));
    mainPanel.setVisible(true);
  }

  private String freshTableName() {
    int index = 0;
    String name = "table";
    while (document.tables.get(name) != null) {
      name = "table" + (++index);
    }
    return name;
  }

  private void save() {
//    final JFrame frame = new JFrame("Sedmoy");
//    final FileDialog fileDialog = new FileDialog(frame);
//    frame.add(fileDialog);
//    frame.setVisible(true);
//    final String file = fileDialog.getFile();
    try {
      final Path tempFile = Files.createTempFile("sedmoy-save-", ".sdm");
      final FileOutputStream outputStream = new FileOutputStream(
          tempFile.toFile());
      document.toJson(outputStream);
      System.out.println("Document saved to " + tempFile);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  private void updateDocument() {
    for (String title : document.tableNames) {
      final Table table = document.tables.get(title);
      final JTextComponent numberLinesComponent =
          graphicalComponents.getNumberLinesComponent(title);
      final JTextComponent numberColumnsComponent =
          graphicalComponents.getNumberColumnsComponent(title);
      table.setDimension(new com.github.romainbrenguier.sedmoy.model.Dimension(
          safeParseInt(numberLinesComponent.getText()).orElse(1),
          safeParseInt(numberColumnsComponent.getText()).orElse(1)));
      if (table instanceof FormulaTable) {
        final JTextComponent component = graphicalComponents
            .getFormulaComponent(title);
        final FormulaTable formulaTable = (FormulaTable) table;
        if (component != null) {
          formulaTable.setGroovyScript(component.getText());
        }
      }
    }
  }

  private Optional<Integer> safeParseInt(String text) {
    try {
      return Optional.of(Integer.parseInt(text));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  private void evaluate() {
    mainPanel.setVisible(false);
    updateDocument();
    final Document evaluatedDocument = tableEvaluator.evaluate(document);
    System.out.println("evaluate");
    fillDocumentComponents(document, evaluatedDocument);
    mainPanel.setVisible(true);
  }

  private void fillDocumentComponents(Document document, Document evaluation) {
    for (String title : document.tableNames) {
      graphicalComponents.getTitleComponent(title).setText(title);
      final Table table = document.tables.get(title);

      graphicalComponents.getNumberLinesComponent(title)
          .setText(Integer.toString(table.getDimension().numberOfLines));
      graphicalComponents.getNumberColumnsComponent(title)
          .setText(Integer.toString(table.getDimension().numberOfColumns));

      if (table instanceof FormulaTable) {
        graphicalComponents.getFormulaComponent(title)
            .setText(((FormulaTable) table).getGroovyScript());
        final DataTableModel model = DataTableModel
            .nonEditable((DataTable) evaluation.tables.get(title));
        graphicalComponents.getTableComponent(title).setModel(model);
      } else {
        final DataTableModel model =
            DataTableModel.editable(((DataTable) table));
        graphicalComponents.getTableComponent(title).setModel(model);
      }
    }
  }
}
