package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import com.fedevela.asic.util.TypeCast;
import com.fedevela.engine.ConfigureEngine;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.regex.Pattern;
import javax.swing.Action;
import javax.swing.InputVerifier;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class FieldUI extends javax.swing.JPanel {

    public enum CaseSensitive {

        NONE,
        UPPER,
        LOWER
    };
    private static final Integer _defaultWidth = 110;
    private static final Integer _defaultHeight = 20;
    private Border _borderDefault;
    private Boolean allowBlank;
    private Integer maxLength;
    private Integer minLength;
    private Boolean doubleTyping;
    private Long doccod;
    private String pattern;
    private Boolean focus = false;
    private FieldUI.CaseSensitive caseSensitive = FieldUI.CaseSensitive.NONE;
    private Pattern regexPattern;
    private Boolean doubleTypingEnter;
    //
    private ConfigureEngine _ce;
    private javax.swing.GroupLayout _layout;
    private javax.swing.JFormattedTextField _field;
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
        _field = new javax.swing.JFormattedTextField();
        _field.setEnabled(!_ce.isDisabled());
        _borderDefault = _field.getBorder();
        /* Init */
        setPreferredSize(new java.awt.Dimension(_width, _height));
        /* Field init */
        _field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isDoubleTypingEnter() != null) {
                    if (!isDoubleTypingEnter()) {
                        checkValidate();
                    }
                } else {
                    checkValidate();
                }
                focus = true;
            }

            @Override
            public void focusLost(FocusEvent e) {
                focus = false;
            }
        });
        _field.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                //checkValidate();
                if (isDoubleTypingEnter() != null) {
                    if (!isDoubleTypingEnter()) {
                        checkValidate();
                    }
                } else {
                    checkValidate();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                switch (caseSensitive) {
                    case LOWER:
                        e.setKeyChar(Character.toLowerCase(e.getKeyChar()));
                        break;
                    case UPPER:
                        e.setKeyChar(Character.toUpperCase(e.getKeyChar()));
                        break;
                    default:
                        break;
                }
                Object value;
                try {
                    if (_field.getFormatter() != null) {
                        value = _field.getFormatter().stringToValue(_field.getText());
                    } else {
                        value = TypeCast.isBlank(_field.getText()) ? null : _field.getText();
                    }
                } catch (ParseException ex) {
                    setUserValid("Valor no válido.");
                    return;
                }
                if (maxLength != null && value != null && maxLength <= TypeCast.toString(value, "").length()) {
                    e.consume();
                }
            }
        });
        allowBlank = true;
        pattern = _ce != null ? _ce.getPattern() : null;

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

    public FieldUI(ConfigureEngine _ce) {
        super();
        this._ce = _ce;
        _initialize();
    }

    public final void checkValidate() {
        Object value;
        try {
            if (_field.getFormatter() != null) {
                value = _field.getFormatter().stringToValue(_field.getText());
            } else {
                value = TypeCast.isBlank(_field.getText()) ? null : _field.getText();
            }
        } catch (ParseException ex) {
            setUserValid("Valor no válido.");
            return;
        }
        if (value == null && !allowBlank) {
            setUserValid("El valor del campo es requerido.");
            return;
        }
        if (minLength != null && value != null && minLength > TypeCast.toString(value, "").length()) {
            setUserValid("El tamaño mínimo del campo es " + minLength + ".");
            return;
        }
        if (maxLength != null && value != null && maxLength < TypeCast.toString(value, "").length()) {
            setUserValid("El tamaño máximo del campo es " + maxLength + ".");
            return;
        }
        setUserValid(null);
    }

    public void setFocusLostBehavior(int i) {
        _field.setFocusLostBehavior(i); //To change body of generated methods, choose Tools | Templates.
    }

    public int getFocusLostBehavior() {
        return _field.getFocusLostBehavior(); //To change body of generated methods, choose Tools | Templates.
    }

    public void setFormatterFactory(JFormattedTextField.AbstractFormatterFactory aff) {
        _field.setFormatterFactory(aff); //To change body of generated methods, choose Tools | Templates.
    }

    public JFormattedTextField.AbstractFormatterFactory getFormatterFactory() {
        return _field.getFormatterFactory(); //To change body of generated methods, choose Tools | Templates.
    }

    //    protected void setFormatter(JFormattedTextField.AbstractFormatter af) {
//        _field.setFormatter(af); //To change body of generated methods, choose Tools | Templates.
//    }
    public JFormattedTextField.AbstractFormatter getFormatter() {
        return _field.getFormatter(); //To change body of generated methods, choose Tools | Templates.
    }

    public void setValue(Object o) {
        _field.setValue(o); //To change body of generated methods, choose Tools | Templates.
    }

    public Object getValue() {
        return _field.getValue(); //To change body of generated methods, choose Tools | Templates.
    }

    public void commitEdit() throws ParseException {
        _field.commitEdit(); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isEditValid() {
        return _field.isEditValid(); //To change body of generated methods, choose Tools | Templates.
    }

    //    protected void invalidEdit() {
//        _field.invalidEdit(); //To change body of generated methods, choose Tools | Templates.
//    }
//    @Override
//    protected void processInputMethodEvent(InputMethodEvent ime) {
//        super.processInputMethodEvent(ime); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    protected void processFocusEvent(FocusEvent fe) {
//        super.processFocusEvent(fe); //To change body of generated methods, choose Tools | Templates.
//    }
    public Action[] getActions() {
        return _field.getActions(); //To change body of generated methods, choose Tools | Templates.
    }

    //    @Override
//    public String getUIClassID() {
//        return super.getUIClassID(); //To change body of generated methods, choose Tools | Templates.
//    }
    public void setDocument(Document dcmnt) {
        _field.setDocument(dcmnt); //To change body of generated methods, choose Tools | Templates.
    }

    public Document getDocument() {
        return _field.getDocument(); //To change body of generated methods, choose Tools | Templates.
    }

    public String getText(int i, int i1) throws BadLocationException {
        return _field.getText(i, i1); //To change body of generated methods, choose Tools | Templates.
    }

    public void cut() {
        _field.cut(); //To change body of generated methods, choose Tools | Templates.
    }

    public void copy() {
        _field.copy(); //To change body of generated methods, choose Tools | Templates.
    }

    public void paste() {
        _field.paste(); //To change body of generated methods, choose Tools | Templates.
    }

    public void setText(String string) {
        _field.setText(string); //To change body of generated methods, choose Tools | Templates.
    }

    public String getText() {
        return _field.getText(); //To change body of generated methods, choose Tools | Templates.
    }

    public String getSelectedText() {
        return _field.getSelectedText(); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isEditable() {
        return _field.isEditable(); //To change body of generated methods, choose Tools | Templates.
    }

    public void setEditable(boolean bln) {
        _field.setEditable(bln); //To change body of generated methods, choose Tools | Templates.
    }

    public void selectAll() {
        _field.selectAll(); //To change body of generated methods, choose Tools | Templates.
    }

    public int getHorizontalAlignment() {
        return _field.getHorizontalAlignment(); //To change body of generated methods, choose Tools | Templates.
    }

    public void setHorizontalAlignment(int i) {
        _field.setHorizontalAlignment(i); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void addKeyListener(KeyListener kl) {
        _field.addKeyListener(kl);
    }

    @Override
    public synchronized void addFocusListener(FocusListener fl) {
        _field.addFocusListener(fl);
    }

    @Override
    public void requestFocus() {
        _field.requestFocus(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean requestFocus(boolean bln) {
        return _field.requestFocus(bln); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean requestFocusInWindow() {
        return _field.requestFocusInWindow(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean requestFocusInWindow(boolean bln) {
        return super.requestFocusInWindow(bln); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setInputVerifier(InputVerifier iv) {
        _field.setInputVerifier(iv); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public InputVerifier getInputVerifier() {
        return _field.getInputVerifier(); //To change body of generated methods, choose Tools | Templates.
    }

    // ###################################################################################################
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Boolean isAllowBlank() {
        return allowBlank;
    }

    public void setAllowBlank(Boolean allowBlank) {
        this.allowBlank = allowBlank;
    }

    public Boolean isDoubleTyping() {
        return doubleTyping;
    }

    public void setDoubleTyping(Boolean doubleTyping) {
        this.doubleTyping = doubleTyping;
    }

    public Boolean isUserValid() {
        return _borderDefault.equals(_field.getBorder());
    }

    public Long getDoccod() {
        return doccod;
    }

    public void setDoccod(Long doccod) {
        this.doccod = doccod;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Boolean isFocus() {
        return focus;
    }

    public FieldUI.CaseSensitive getCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(FieldUI.CaseSensitive caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public void setCaseSensitive(String caseSensitive) {
        try {
            this.caseSensitive = FieldUI.CaseSensitive.valueOf(caseSensitive);
        } catch (IllegalArgumentException ex) {
            this.caseSensitive = FieldUI.CaseSensitive.NONE;
        }
    }

    public Pattern getRegexPattern() {
        return regexPattern;
    }

    public void setRegexPattern(Pattern regexPattern) {
        this.regexPattern = regexPattern;
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = Pattern.compile(regexPattern);
    }

    public void setUserValid(String valid) {
        _field.setToolTipText(valid);
        _field.setBorder(TypeCast.isBlank(valid) ? _borderDefault : javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
    }

    /**
     * @return the doubleTypingEnter
     */
    public Boolean isDoubleTypingEnter() {
        return doubleTypingEnter;
    }

    /**
     * @param doubleTypingEnter the doubleTypingEnter to set
     */
    public void setDoubleTypingEnter(Boolean doubleTypingEnter) {
        this.doubleTypingEnter = doubleTypingEnter;
    }

    /**
     * Este metodo es con la finalidad de asignar tooltip y border en su forma
     * inicial y que pueda ser llamado desde otros componentes
     */
    public void resetToolTipBorder() {
        _field.setToolTipText("");
        _field.setBorder(_borderDefault);

    }

    //    public Integer getLabelWidth() {
    //        return _label == null ? null : _label.getWidth();
    //    }
    //
    //    public String getLabelText() {
    //        return _label == null ? null : _label.getText();
    //    }
    public JLabel getLabel() {
        return _label;
    }

    public void setLabel(JLabel _label) {
        this._label = _label;
    }

    public void setLabel(String _label) {
        this._label = new javax.swing.JLabel(_label);
    }

    public ConfigureEngine getCe() {
        return _ce;
    }
}
