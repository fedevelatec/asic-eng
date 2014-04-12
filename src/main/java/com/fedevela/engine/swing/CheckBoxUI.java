package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import com.fedevela.engine.ConfigureEngine;
import java.awt.Dimension;
import javax.accessibility.Accessible;
import javax.swing.JCheckBox;

public class CheckBoxUI extends JCheckBox implements Accessible {

    private static final Integer _defaultWidth = 110;
    private static final Integer _defaultHeight = 20;
    private String key;
    private ConfigureEngine _ce;

    private void _initialize() {
        _ce = _ce == null ? new ConfigureEngine() : _ce;
        _ce.setWidth(_ce.getWidth() != null ? _ce.getWidth() : _defaultWidth);
        _ce.setHeight(_ce.getHeight() != null ? _ce.getHeight() : _defaultHeight);

        setText(_ce.getText() == null ? "(none)" : _ce.getText());// (conf.isTextWrap()) ? "<html>" + definition.getDescription() + "</html>" : definition.getDescription()

        setPreferredSize(new Dimension(_ce.getWidth(), _ce.getHeight()));
        setSize(getPreferredSize());
    }

    public CheckBoxUI(ConfigureEngine _ce) {
        super();
        this._ce = _ce;
        _initialize();
    }

    public CheckBoxUI() {
        super();
        _initialize();
    }

    public CheckBoxUI(String text) {
        super();
        _ce = new ConfigureEngine();
        _ce.setText(text);
        _initialize();
    }

    public CheckBoxUI(String text, boolean selected) {
        this(text);
        setSelected(selected);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
