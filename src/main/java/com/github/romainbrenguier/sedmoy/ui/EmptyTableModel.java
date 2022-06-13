package com.github.romainbrenguier.sedmoy.ui;

import javax.swing.table.AbstractTableModel;

public class EmptyTableModel extends AbstractTableModel {

  @Override
  public int getRowCount() {
    return 0;
  }

  @Override
  public int getColumnCount() {
    return 0;
  }

  @Override
  public Object getValueAt(int i, int i1) {
    return null;
  }

}
