package Auxiliares;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class TableModel_withClases extends AbstractTableModel {
        
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private Vector<String> columnas = new Vector<String>();
		private Vector<Vector<Object>> datos = new Vector<Vector<Object>>();
		private Vector<Boolean> editable = new Vector<Boolean>();
		
		
		public TableModel_withClases(String[] column){
			for (int i = 0; i < column.length; i++) {
				this.appendColumna(column[i],true);
			}
			
		};
		

		
 
        public int getColumnCount() {
            return columnas.size();
        }
 
        public int getRowCount() {
            return datos.get(0).size();
        }
 
        public void appendColumna(String n,  boolean isEditable){
        	columnas.add(n);
        	datos.add(new Vector<Object>());
        	editable.add(isEditable);
        	
        }

        public void addRow (Object[] dat){
        	for (int i = 0; i < columnas.size(); i++) {
				datos.get(i).add(dat[i]);			
			}
        	
        }
        
        public String getColumnName(int col) {
            return columnas.get(col);
        }
 
        public Object getValueAt(int row, int col) {
            return datos.get(col).get(row);
        }
 
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
        	if (datos.size()==0)
        		return String.class;
        	else
        		return datos.get(c).get(0).getClass();
        }
 
        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
                return editable.get(col);
        }
 
        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
 
            datos.get(col).set(row, value);
            // Normally, one should call fireTableCellUpdated() when 
            // a value is changed.  However, doing so in this demo
            // causes a problem with TableSorter.  The tableChanged()
            // call on TableSorter that results from calling
            // fireTableCellUpdated() causes the indices to be regenerated
            // when they shouldn't be.  Ideally, TableSorter should be
            // given a more intelligent tableChanged() implementation,
            // and then the following line can be uncommented.
            // fireTableCellUpdated(row, col);
 
        }
	
	
}
