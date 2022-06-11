package com.github.romainbrenguier.sedmoy.ui;

import com.github.romainbrenguier.sedmoy.model.Document;
import com.github.romainbrenguier.sedmoy.model.Table;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class GraphicalInterface {

  private final Document document;

  public GraphicalInterface(Document document) {
    this.document = document;
  }

  public void run() {
    final JFrame frame = new JFrame("Sedmoy");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    final String title = document.tableNames.get(0);
    final Table table = document.tables.get(title);
    final JTextArea textArea = new JTextArea();
    textArea.append(table.toString());
    textArea.setPreferredSize(new Dimension(300, 300));
    frame.getContentPane().add(textArea, BorderLayout.CENTER);

    frame.setLocationRelativeTo(null);
    frame.pack();
    frame.setVisible(true);
  }

}
