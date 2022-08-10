package com.github.romainbrenguier.sedmoy.ui;

import com.github.romainbrenguier.sedmoy.model.DataTable;

import javax.swing.table.AbstractTableModel;

/**
 * Adapter from {@link DataTable} to swing's {@link
 * javax.swing.table.TableModel}.
 */
public class DataTableModel extends AbstractTableModel {

  private final DataTable dataTable;
  private final boolean editable;

  private DataTableModel(DataTable dataTable, boolean editable) {
    this.dataTable = dataTable;
    this.editable = editable;
  }

  public static DataTableModel editable(DataTable dataTable) {
    return new DataTableModel(dataTable, true);
  }

  public static DataTableModel nonEditable(DataTable dataTable) {
    return new DataTableModel(dataTable, false);
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

  @Override
  public boolean isCellEditable(int lineIndex, int columnIndex) {
    return editable;
  }

  @Override
  public void setValueAt(Object value, int lineIndex, int columnIndex) {
    dataTable.set(columnIndex, lineIndex, value.toString());
  }
}
