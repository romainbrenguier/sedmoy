package com.github.romainbrenguier.sedmoy.ui;

import com.github.romainbrenguier.sedmoy.app.GroovyInterpreter;
import com.github.romainbrenguier.sedmoy.app.TableEvaluator;
import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Document;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import com.github.romainbrenguier.sedmoy.model.Table;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class GraphicalInterface {

  private final Document document;

  private final TableEvaluator tableEvaluator = new TableEvaluator(new GroovyInterpreter());
  private JPanel mainPanel;

  private Map<String, JTextArea> formulaAreas = new HashMap<>();

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
    final Document evaluatedDocument = tableEvaluator.evaluate(document);
    addDocumentComponents(mainPanel, document, evaluatedDocument);

    frame.add(BorderLayout.CENTER, mainPanel);
    frame.setLocationRelativeTo(null);
    frame.pack();
    frame.setVisible(true);
  }

  private void updateDocument() {
    formulaAreas.forEach((title, textArea) ->
        {
          final Table table = document.tables.get(title);
          if (table instanceof FormulaTable) {
            ((FormulaTable) table).setGroovyScript(textArea.getText());
          }
        }
    );
  }

  private void evaluate() {
    mainPanel.removeAll();
    updateDocument();
    final Document evaluatedDocument = tableEvaluator.evaluate(document);
    System.out.println("evaluate");
    addDocumentComponents(mainPanel, document, evaluatedDocument);
  }

  private void addDocumentComponents(Container frame, Document document, Document evaluation) {
    for (String title : document.tableNames) {
      final Table table = document.tables.get(title);
      if (table instanceof FormulaTable) {
        final Table evaluatedTable = evaluation.tables.get(title);
        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            tableComponent(title, table),
            tableComponent(null, evaluatedTable));
        frame.add(splitPane);
      } else {
        frame.add(tableComponent(title, table));
      }
    }
  }

  private Component tableComponent(String title, Table table) {
    final JComponent component;
    if (table instanceof FormulaTable) {
      JTextArea textArea = new JTextArea();
      textArea.append(((FormulaTable) table).getGroovyScript());
      textArea.setMargin(new Insets(0, 2, 0, 2));
      textArea.setBorder(BorderFactory.createLineBorder(Color.black));
      component = textArea;
      if (title != null) {
        formulaAreas.put(title, textArea);
      }
    } else if (table instanceof DataTable) {
      final DataTableModel model = new DataTableModel((DataTable) table);
      final JTable jTable = new JTable(model);
      component = jTable;
    } else {
      JTextArea textArea = new JTextArea();
      textArea.append(table.toString());
      component = textArea;
    }
    final JScrollPane pane = new JScrollPane(component);
    pane.setPreferredSize(new Dimension(200, 200));
    return pane;
  }
}
