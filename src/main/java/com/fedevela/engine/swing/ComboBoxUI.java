package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import com.fedevela.engine.ConfigureEngine;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class ComboBoxUI extends javax.swing.JPanel {

    private static final Integer _defaultWidth = 110;
    private static final Integer _defaultHeight = 25;
    private Border _defaultBorder;
    //
    private ConfigureEngine _ce;
    private javax.swing.GroupLayout _layout;
    private javax.swing.JComboBox _field;
    private javax.swing.JLabel _label;
    private Integer _width;
    private Integer _height;

    private void _initialize() {
        _ce = _ce == null ? new ConfigureEngine() : _ce;
        _width = _ce.getWidth() != null ? _ce.getWidth() : _defaultWidth;
        _height = _ce.getHeight() != null ? _ce.getHeight() : _defaultHeight;
        _ce.setWidth(_width);
        _ce.setHeight(_height);
        _layout = new javax.swing.GroupLayout(this);
        if (_ce != null && _ce.getLabel() != null) {
            _label = new javax.swing.JLabel(_ce.getLabel().getText());
            if (_ce.getLabel().getWidth() != null) {
                _width = _width.compareTo(_ce.getLabel().getWidth()) >= 0 ? _width : _ce.getLabel().getWidth();
            }
            if (_ce.getLabel().getHeight() != null) {
                _height = _height.compareTo(_ce.getLabel().getHeight()) >= 0 ? _height : _ce.getLabel().getHeight();
            } else {
                _height += _defaultHeight;
            }
        }
        _field = new javax.swing.JComboBox();
        _defaultBorder = _field.getBorder();
        /* Init */
        setPreferredSize(new java.awt.Dimension(_width, _height));
        /* Field init */

        /* Layout init */
        setLayout(_layout);
        if (_label != null) {
            _layout.setHorizontalGroup(
                    _layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(_field, javax.swing.GroupLayout.PREFERRED_SIZE, _ce.getWidth(), javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
            _layout.setVerticalGroup(
                    _layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(_layout.createSequentialGroup()
                                    .addComponent(_label)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(_field, javax.swing.GroupLayout.PREFERRED_SIZE, _ce.getHeight(), javax.swing.GroupLayout.PREFERRED_SIZE)));
        } else {
            _layout.setHorizontalGroup(
                    _layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(_field, javax.swing.GroupLayout.PREFERRED_SIZE, _ce.getWidth(), javax.swing.GroupLayout.PREFERRED_SIZE));
            _layout.setVerticalGroup(
                    _layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(_field, javax.swing.GroupLayout.PREFERRED_SIZE, _ce.getHeight(), javax.swing.GroupLayout.PREFERRED_SIZE));
        }
        setSize(getPreferredSize());
    }

    public ComboBoxUI(ConfigureEngine _ce) {
        super();
        this._ce = _ce;
        _initialize();
    }

    public javax.swing.ComboBoxModel getModel() {
        return _field.getModel();
    }

    public void setSelectedItem(Object item) {
        _field.setSelectedItem(item);
    }

    public void setItem(Object[] items) {
        _field.setModel(items == null ? new DefaultComboBoxModel() : new DefaultComboBoxModel(items));
    }

    public Object getSelected() {
        return _field.getSelectedItem();
    }

    public JLabel getLabel() {
        return _label;
    }

    @Override
    public void setEnabled(boolean enabled) {
        _field.setEnabled(enabled);
    }

    @Override
    public boolean requestFocusInWindow() {
        return _field.requestFocusInWindow();
    }

    public void addItemListener(java.awt.event.ItemListener il) {
        _field.addItemListener(il);
    }

}
