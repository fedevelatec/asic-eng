package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import com.fedevela.asic.util.TypeCast;
import com.fedevela.engine.ConfigureEngine;
import com.fedevela.engine.EngineException;
import com.fedevela.engine.swing.grid.Column;
import com.fedevela.engine.swing.grid.Model;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

public class GridUI extends javax.swing.JPanel {

    private static final Integer _defaultWidth = 120;
    private static final Integer _defaultHeight = 80;
    private javax.swing.JScrollPane _scroll;
    private javax.swing.JTable _table;
    private javax.swing.JLabel _lblRowCount;
    private javax.swing.GroupLayout _layout;
    private ButtonUI _btnAdd;
    private ButtonUI _btnEdit;
    private ButtonUI _btnRemove;
    private Model _model;
    // Determina el nombre del campo llave del grid. Su valor por DEFAULT es NULL o EMPTY.
    private String _id;
    private ConfigureEngine _ce;
    private Boolean scrollVisible = false;

    private void _initialize() {
        _ce = _ce == null ? new ConfigureEngine() : _ce;
        _ce.setWidth(_ce.getWidth() != null ? _ce.getWidth() : _defaultWidth);
        _ce.setHeight(_ce.getHeight() != null ? _ce.getHeight() : _defaultHeight);
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        _scroll = new javax.swing.JScrollPane();
        _table = new javax.swing.JTable();
        _lblRowCount = new javax.swing.JLabel("Total: 0");
        _btnAdd = new ButtonUI();
        _btnEdit = new ButtonUI();
        _btnRemove = new ButtonUI();
        _layout = new javax.swing.GroupLayout(this);
        _model = new Model(_ce.getColumns());
        _id = _ce.getColumns().getId();
        /* Init */
        setPreferredSize(new Dimension(_ce.getWidth(), _ce.getHeight()));
        /* Table init */
        _table.setModel(_model);
        Integer idx = 0;
        for (Column c : _ce.getColumns().getColumns()) {
            TableColumn tc = _table.getColumnModel().getColumn(idx);
            tc.setResizable(c.isResizable());
            if (!TypeCast.isBlank(c.getWidth())) {
                Integer width;
                if (c.getWidth().endsWith("%")) {
                    width = TypeCast.toInteger(c.getWidth().replaceAll("%", ""));
                    width = (width * _ce.getWidth()) / 100;
                } else {
                    width = TypeCast.toInteger(c.getWidth());
                }
                tc.setWidth(width.intValue());
                tc.setMinWidth(width.intValue());
                tc.setMaxWidth(width.intValue());
                tc.setPreferredWidth(width.intValue());
            }
            idx++;
        }
        _table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                int row = _table.rowAtPoint(p);
                int col = _table.columnAtPoint(p);
                _table.setToolTipText(String.valueOf(_table.getValueAt(row, col)));
            }
        });

        _table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2 && _btnEdit.isEnabled()) {
                    _btnEdit.doClick();
                }
            }
        });
        _model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tme) {
                _lblRowCount.setText("Total: " + _model.getRowCount());
            }
        });
        /* Scroll init */
        _scroll.setViewportView(_table);
        /* Button init */
        _btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adeamx/adeadms/images/add.png"))); // NOI18N
        _btnAdd.setEnabled(false);

        _btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adeamx/adeadms/images/icon-edit-doc.png"))); // NOI18N
        _btnEdit.setEnabled(false);

        _btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adeamx/adeadms/images/cancel16x16.png"))); // NOI18N
        _btnRemove.setEnabled(false);

        _btnRemove.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _model.removeRow(_table.getSelectedRow());
            }
        });
        /* Layout init */
        setLayout(_layout);
        /* HORIZONTAL GROUP */
        _layout.setHorizontalGroup(
                _layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(_layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(_layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(_layout.createSequentialGroup()
                                                .addComponent(_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, _ce.getWidth() - 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(_layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(_btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(_btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(_btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(_lblRowCount))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        /* VERTICAL GROUP */
        _layout.setVerticalGroup(
                _layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(_layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(_layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(_scroll, javax.swing.GroupLayout.PREFERRED_SIZE, _ce.getHeight() - 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(_layout.createSequentialGroup()
                                                .addComponent(_btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(_btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(_btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(_lblRowCount)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        setSize(getPreferredSize());
    }

    public GridUI(ConfigureEngine _ce) {
        super();
        this._ce = _ce;
        _initialize();
    }

    public <T extends Serializable> void addRow(T row) {
        _model.add(row);
        _btnAdd.setEnabled(false);
        _scroll.getViewport().setViewPosition(new Point(0, _table.getCellRect(_model.getRowCount() - 1, 0, true).y));
    }

    public <T extends Serializable> void addRows(List<T> rows) {
        _model.add(rows);
        _btnAdd.setEnabled(false);
    }

    public <T extends Serializable> void addRows(Set<T> rows) {
        _model.add(rows);
        _btnAdd.setEnabled(false);
    }

    public <T extends Serializable> Boolean existRow(T row) {
        return _model.contains(row);
    }

    public Boolean existValue(Object value) throws EngineException {
        if (TypeCast.isBlank(_id)) {
            throw new EngineException("La llave primaria del grid es nula o vacía.");
        }
        return _model.contains(value, _id);
    }

    public Object getSelected() {
        return _model.getRowValue(_table.getSelectedRow());
    }

    public void addRowSelectedAction(javax.swing.event.ListSelectionListener lsl) {
        _table.getSelectionModel().addListSelectionListener(lsl);
    }

    public void setEnabledBtnAdd(Boolean enabled) {
        _btnAdd.setEnabled(enabled);
    }

    public void addBtnAddAction(java.awt.event.ActionListener al) {
        _btnAdd.addActionListener(al);
    }

    public void setEnabledBtnEdit(Boolean enabled) {
        _btnEdit.setEnabled(enabled);
    }

    public void addBtnEditAction(java.awt.event.ActionListener al) {
        _btnEdit.addActionListener(al);
    }

    public void setEnabledBtnRemove(Boolean enabled) {
        _btnRemove.setEnabled(enabled);
    }

    public void addBtnRemoveAction(java.awt.event.ActionListener al) {
        _btnRemove.addActionListener(al);
    }

    public List<Serializable> getListModelData() {
        return _model.getData();
    }

    public Boolean isScrollVisible() {
        return scrollVisible;
    }

    public void setScrollVisible(Boolean scrollVisible) {
        this.scrollVisible = scrollVisible;
    }

}
