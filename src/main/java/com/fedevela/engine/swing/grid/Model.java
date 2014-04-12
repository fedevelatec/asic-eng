package com.fedevela.engine.swing.grid;

/**
 * Created by fvelazquez on 11/04/14.
 */
import com.fedevela.asic.util.TypeCast;
import com.fedevela.engine.EngineException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.codicentro.core.CDCException;
import net.codicentro.core.Utils;

public class Model extends javax.swing.table.AbstractTableModel implements Serializable {

    private static final long serialVersionUID = 1L;
    final List<Boolean> edits = new ArrayList<Boolean>();
    private final List<Serializable> data;
    private final ColumnModel cm;

    public Model(ColumnModel cm) {
        this.cm = cm;
        data = new ArrayList<Serializable>();
        for (Column c : cm.getColumns()) {
            edits.add(c.isEditable());
        }
    }

    public <T extends Serializable> void add(T row) {
        data.add(row);
        fireTableDataChanged();
    }

    public <T extends Serializable> void add(List<T> rows) {
        for (Serializable row : rows) {
            add(row);
        }
    }

    public <T extends Serializable> void add(Set<T> rows) {
        for (Serializable row : rows) {
            add(row);
        }
    }

    @Override
    public int getRowCount() {
        return data == null ? 0 : data.size();
    }

    public Object getRowValue(int row) {
        return data != null && !data.isEmpty() && row >= 0 && row < data.size() ? data.get(row) : null;
    }

    @Override
    public int getColumnCount() {
        return cm != null && cm.getColumns() != null ? cm.getColumns().size() : 0;
    }

    @Override
    public Object getValueAt(int row, int col) {
        String dataIndex = cm.getColumns().get(col).getName();
        if (TypeCast.isBlank(dataIndex)) {
            throw new RuntimeException("No data index found. " + cm.getColumns().get(col));
        }
        String[] keys = dataIndex.split("\\.");
        Object rs = data.get(row);
        try {
            for (int idx = 0; rs != null && idx < keys.length; idx++) {
                Field field = rs.getClass().getDeclaredField(keys[idx]);
                Boolean accessible = field.isAccessible();
                field.setAccessible(true);
                rs = field.get(rs);
                field.setAccessible(accessible);
            }
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        return rs;
    }

    @Override
    public String getColumnName(int i) {
        return cm != null && cm.getColumns() != null && !cm.getColumns().isEmpty() ? cm.getColumns().get(i).getHeader() : null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return edits != null || !edits.isEmpty() ? edits.get(columnIndex) : true;
    }

    public void removeRow(int row) {
        if (data != null && !data.isEmpty() && data.size() >= 0 && row < data.size()) {
            data.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }

    public <T extends Serializable> Boolean contains(T row) {
        return data.contains(row);
    }

    public Boolean contains(Object value, String field) throws EngineException {
        try {
            return data != null && !data.isEmpty() ? Utils.getElement(data, value, field) != null : false;
        } catch (CDCException ex) {
            throw new EngineException(ex.getMessage());
        }
    }

    public List<Serializable> getData(){
        return data;
    }
}
