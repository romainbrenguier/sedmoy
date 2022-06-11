package com.github.romainbrenguier.sedmoy.ui;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import com.github.romainbrenguier.sedmoy.model.Document;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import com.github.romainbrenguier.sedmoy.model.Table;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class GraphicalInterface {

  private final Document document;

  public GraphicalInterface(Document document) {
    this.document = document;
  }

  public void run() {
    final JFrame frame = new JFrame("Sedmoy");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container contentPanel = frame.getContentPane();
    GroupLayout groupLayout = new GroupLayout(contentPanel);
    contentPanel.setLayout(groupLayout);

//    final JButton save = new JButton("Save");
//    frame.add(save);
//    final JButton evaluate = new JButton("Evaluate");
//    frame.add(evaluate);

    for (String title : document.tableNames) {
      final Table table = document.tables.get(title);
      frame.add(tableComponent(table));
    }

    frame.setLayout(new GridLayout());
    frame.setLocationRelativeTo(null);
    frame.pack();
    frame.setVisible(true);
  }

  private Component tableComponent(Table table) {
    final JComponent component;
    if (table instanceof FormulaTable) {
      JTextArea textArea = new JTextArea();
      textArea.append(((FormulaTable) table).getGroovyScript());
      component = textArea;
    } else if (table instanceof DataTable) {
      final DataTable dataTable = (DataTable) table;
      String array[][] = new String[(dataTable).height()][(dataTable).width()];
      for (int i = 0; i < array.length; ++i) {
        for (int j = 0; j < array[0].length; ++j) {
          array[i][j] = dataTable.cell(j, i).toString();
        }
      }
      final String[] titles = new String[2];
      titles[0] = "1";
      titles[1] = "2";
      final JTable jTable = new JTable(array, titles);
      component = jTable;
    } else {
      JTextArea textArea = new JTextArea();
      textArea.append(table.toString());
      component = textArea;
    }
    component.setPreferredSize(new Dimension(600, 600));
    return component;
  }
}
