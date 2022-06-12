package com.github.romainbrenguier.sedmoy.ui;

import com.github.romainbrenguier.sedmoy.model.DataTable;
import javax.swing.table.AbstractTableModel;

/**
 * Adapter from {@link com.github.romainbrenguier.sedmoy.model.DataTable} to swing's {@link
 * javax.swing.table.TableModel}.
 */
public class DataTableModel extends AbstractTableModel {

  private final DataTable dataTable;

  public DataTableModel(DataTable dataTable) {
    this.dataTable = dataTable;
  }

  @Override
  public int getRowCount() {
    return dataTable.height();
  }

  @Override
  public int getColumnCount() {
    return dataTable.width();
  }

  @Override
  public Object getValueAt(int row, int col) {
    return dataTable.cell(col, row);
  }
}
