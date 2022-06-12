package com.github.romainbrenguier.sedmoy.ui;

import com.github.romainbrenguier.sedmoy.app.GroovyInterpreter;
import com.github.romainbrenguier.sedmoy.app.TableEvaluator;
import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Document;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import com.github.romainbrenguier.sedmoy.model.Table;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class GraphicalInterface {

  private final Document document;

  private final TableEvaluator tableEvaluator = new TableEvaluator(new GroovyInterpreter());

  public GraphicalInterface(Document document) {
    this.document = document;
  }

  public void run() {
    final JFrame frame = new JFrame("Sedmoy");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    final GridLayout layoutManager = new GridLayout(0, document.tableNames.size(), 1, 1);
    frame.setLayout(layoutManager);

//    final JButton save = new JButton("Save");
//    frame.add(save);
//    final JButton evaluate = new JButton("Evaluate");
//    frame.add(evaluate);

    addDocumentComponents(frame, document);
    final Document evaluatedDocument = tableEvaluator.evaluate(document);
    addDocumentComponents(frame, evaluatedDocument);

    frame.setLocationRelativeTo(null);
    frame.pack();
    frame.setVisible(true);
  }

  private void addDocumentComponents(JFrame frame, Document document) {
    for (String title : document.tableNames) {
      final Table table = document.tables.get(title);
      frame.add(tableComponent(table));
    }
  }

  private Component tableComponent(Table table) {
    final JComponent component;
    if (table instanceof FormulaTable) {
      JTextArea textArea = new JTextArea();
      textArea.append(((FormulaTable) table).getGroovyScript());
      textArea.setMargin(new Insets(0,2, 0, 2));
      textArea.setBorder(BorderFactory.createLineBorder(Color.black));
      component = textArea;
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
