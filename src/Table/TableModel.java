package Table;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Abdurasyid
 */
public class TableModel extends AbstractTableModel {

    private final String[] columnNames;                                         //variabel yg digunakan utk header tabel
    private final Object[][] data;                                              //data tabel row-column

    /**
     * Deklarasi konstruktor kelas
     *
     * @param columnNames -- header tabel
     * @param data -- data tabel
     */
    public TableModel(String[] columnNames, Object[][] data) {
        this.columnNames = columnNames;
        this.data = data;
    }

    /**
     * Method yg digunakan utk memperoleh jumlah kolom
     *
     * @return -- jumlah kolom
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Method yg digunakan utk memperoleh jumlah baris
     *
     * @return -- jumlah baris
     */
    @Override
    public int getRowCount() {
        return data.length;
    }

    /**
     * Method yg digunakan utk memperoleh header tabel
     *
     * @param col -- indeks posisi header
     * @return -- header
     */
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * Method yg digunakan utk memperoleh data pada baris dan kolom
     *
     * @param row -- posisi baris
     * @param col -- posisi kolom
     * @return -- data tabel
     */
    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
}
