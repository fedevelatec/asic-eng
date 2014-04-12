package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import com.fedevela.asic.util.TypeCast;
import com.fedevela.engine.ConfigureEngine;

public class NumberFieldUI extends FieldUI {

    private Short _decimalPlaces;

    public NumberFieldUI() {
        super(null);
    }

    public NumberFieldUI(ConfigureEngine ce) {
        super(ce);
        _decimalPlaces = ce.getDecimalPlaces();
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                String txtValue = ((javax.swing.JFormattedTextField) evt.getSource()).getText();
                if (!Character.isDigit(evt.getKeyChar())
                        && _decimalPlaces == null) {
                    evt.consume();
                } else if (!Character.isDigit(evt.getKeyChar())
                        && _decimalPlaces != null
                        && (evt.getKeyChar() != '.') || (evt.getKeyChar() == '.' && !TypeCast.isBlank(txtValue) && txtValue.indexOf('.') != -1)) {
                    evt.consume();
                } else if (!TypeCast.isBlank(txtValue)
                        && txtValue.indexOf('.') != -1
                        && !TypeCast.isBlank(txtValue.substring(txtValue.indexOf('.') + 1))
                        && txtValue.substring(txtValue.indexOf('.') + 1).length() >= _decimalPlaces) {
                    evt.consume();
                }
            }
        });
    }

    @Override
    public Object getValue() {
        return _decimalPlaces == null || _decimalPlaces == 0 ? TypeCast.toBigInteger(getText()) : TypeCast.toBigDecimal(getText());
    }

}
