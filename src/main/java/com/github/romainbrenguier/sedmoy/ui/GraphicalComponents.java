package com.github.romainbrenguier.sedmoy.ui;

import com.github.romainbrenguier.sedmoy.app.Maps;
import com.github.romainbrenguier.sedmoy.model.Document;
import com.github.romainbrenguier.sedmoy.model.FormulaTable;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

public class GraphicalComponents {

  private final Map<String, JTextComponent> formulaAreas = new HashMap<>();
  private final Map<String, JTextComponent> numberLinesTextComponent =
      new HashMap<>();
  private final Map<String, JTextComponent> numberColumnsTextComponent =
      new HashMap<>();
  private final Map<String, JTextComponent> titleTextComponents =
      new HashMap<>();
  private final Map<String, JTable> tableComponents = new HashMap<>();

  public JTextComponent getFormulaComponent(String title) {
    return formulaAreas.get(title);
  }

  public JTextComponent getNumberLinesComponent(String title) {
    return numberLinesTextComponent.get(title);
  }

  public JTextComponent getNumberColumnsComponent(String title) {
    return numberColumnsTextComponent.get(title);
  }

  public JTextComponent getTitleComponent(String title) {
    return titleTextComponents.get(title);
  }

  public JTable getTableComponent(String title) {
    return tableComponents.get(title);
  }

  public void initialize(Container frame, Document document) {
    formulaAreas.clear();
    numberLinesTextComponent.clear();
    titleTextComponents.clear();
    tableComponents.clear();
    frame.removeAll();
    frame.setLayout(new GridLayout(0, document.tableNames.size(), 1, 1));
    for (String title : document.tableNames) {
      initializeTable(frame, title,
          document.tables.get(title) instanceof FormulaTable);
    }
  }

  private void initializeTable(
      Container frame, String title, boolean tableIsFormula) {
    final JPanel panel = new JPanel(new BorderLayout());
    final JPanel headerPanel = new JPanel(new FlowLayout());
    final JTextComponent titleText = new JTextField(title);
    headerPanel.add(titleText);
    titleText.setPreferredSize(new Dimension(200, 20));
    titleTextComponents.put(title, titleText);

    final JTextComponent lines = new JTextField();
    lines.setPreferredSize(new Dimension(60, 20));
    headerPanel.add(lines);
    numberLinesTextComponent.put(title, lines);

    final JTextComponent columns = new JTextField();
    columns.setPreferredSize(new Dimension(60, 20));
    headerPanel.add(columns);
    numberColumnsTextComponent.put(title, columns);

    panel.add(BorderLayout.NORTH, headerPanel);

    final JComponent component;
    if (tableIsFormula) {

      final JTextArea textArea = new JTextArea();
      formulaAreas.put(title, textArea);
      final JTable tableComponent = new JTable(new EmptyTableModel());
      tableComponents.put(title, tableComponent);
      component = new JSplitPane(JSplitPane.VERTICAL_SPLIT, wrapComponent(textArea),
          wrapComponent(tableComponent));
    } else {
      final JTable tableComponent = new JTable(new EmptyTableModel());
      tableComponents.put(title, tableComponent);
      component = wrapComponent(tableComponent);
    }

    panel.add(BorderLayout.CENTER, component);
    frame.add(panel);
  }

  private JComponent wrapComponent(JComponent table) {
    final JScrollPane pane = new JScrollPane(table);
    pane.setPreferredSize(new Dimension(200, 200));
    return pane;
  }

  void renameTable(String oldName, String newName) {
    Maps.renameKey(formulaAreas, oldName, newName);
    Maps.renameKey(numberLinesTextComponent, oldName, newName);
    Maps.renameKey(numberColumnsTextComponent, oldName, newName);
    Maps.renameKey(titleTextComponents, oldName, newName);
    Maps.renameKey(tableComponents, oldName, newName);
  }
}
