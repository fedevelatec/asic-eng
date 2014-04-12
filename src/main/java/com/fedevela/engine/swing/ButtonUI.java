package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import javax.accessibility.Accessible;
import javax.swing.JButton;

public class ButtonUI extends JButton implements Accessible {

    public ButtonUI() {
    }

    public ButtonUI(String text) {
        super(text);
    }

    public void setEnabled(Boolean b) {
        super.setEnabled(b);
    }
}
