package com.github.romainbrenguier.sedmoy.ui;

import com.github.romainbrenguier.sedmoy.app.GroovyInterpreter;
import com.github.romainbrenguier.sedmoy.app.TableEvaluator;
import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Document;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import com.github.romainbrenguier.sedmoy.model.Table;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

public class GraphicalInterface {

  private final Document document;

  private final TableEvaluator tableEvaluator = new TableEvaluator(new GroovyInterpreter());
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
    final JButton save = new JButton("Save");
    toolPanel.add(save);
    final JButton evaluateButton = new JButton("Evaluate");
    evaluateButton.addActionListener(actionEvent -> evaluate());
    toolPanel.add(evaluateButton);
    frame.add(BorderLayout.NORTH, toolPanel);

    final GridLayout layoutManager = new GridLayout(0, document.tableNames.size(), 1, 1);
    mainPanel = new JPanel(layoutManager);

    graphicalComponents.initialize(mainPanel, document);
    final Document evaluatedDocument = tableEvaluator.evaluate(document);
    fillDocumentComponents(document, evaluatedDocument);

    frame.add(BorderLayout.CENTER, mainPanel);
    frame.setLocationRelativeTo(null);
    frame.pack();
    frame.setVisible(true);
  }

  private void updateDocument() {
    for (String title : document.tableNames) {
      final Table table = document.tables.get(title);
      if (table instanceof FormulaTable) {
        final JTextComponent component = graphicalComponents.getFormulaComponent(title);
        final FormulaTable formulaTable = (FormulaTable) table;
        if (component != null) {
          formulaTable.setGroovyScript(component.getText());
        }
        final JTextComponent sizeComponent = graphicalComponents.getFormulaSizeComponent(title);
        formulaTable.withDimension(new com.github.romainbrenguier.sedmoy.model.Dimension(
            Integer.parseInt(sizeComponent.getText()),
            formulaTable.getDimension().numberOfColumns));
      }
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

      if (table instanceof FormulaTable) {
        graphicalComponents.getFormulaComponent(title)
            .setText(((FormulaTable) table).getGroovyScript());
        graphicalComponents.getFormulaSizeComponent(title)
            .setText(Integer.toString(((FormulaTable) table).getDimension().numberOfLines));
        graphicalComponents.getTableComponent(title)
            .setModel(new DataTableModel((DataTable) evaluation.tables.get(title)));
      } else {
        graphicalComponents.getTableComponent(title).setModel(new DataTableModel(((DataTable) table)));
      }
    }
  }
}
