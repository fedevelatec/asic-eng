package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.apache.commons.lang.ObjectUtils;

public class DoubleTypingUI extends javax.swing.JDialog {

    private String confirm;

    public DoubleTypingUI(Component owner, final FieldUI fld) {
        super();
        // setUndecorated(true);
        //setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setTitle("Doble Tipeo");
        setModal(true);
        getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirm = "Se ha cancelado la acción del doble tipeo.";
                setVisible(false);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        JPanel jp = new JPanel();
        jp.setLayout(null);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jp, javax.swing.GroupLayout.DEFAULT_SIZE, 10 + fld.getWidth(), Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jp, javax.swing.GroupLayout.DEFAULT_SIZE, 10 + fld.getHeight(), Short.MAX_VALUE).addContainerGap()));

        /**
         * INPUT
         */
        final FieldUI ifl;
        if (fld instanceof NumberFieldUI) {
            ifl = new NumberFieldUI(fld.getCe());
        } else if (fld instanceof DateFieldUI) {
            ifl = new DateFieldUI(fld.getCe());
        } else {
            ifl = new TextFieldUI(fld.getCe());
        }
        ifl.setLocation(10, 10);
        ifl.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                if (ObjectUtils.equals(ifl.getText(), fld.getText())) {
                    confirm = null;
                } else {
                    confirm = "No coincide el valor del campo con el de la recaptura.";
                }
                ifl.setUserValid(confirm);
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    setVisible(false);
                }
            }
        });
        jp.add(ifl);
        /**
         *
         */
        setLocationRelativeTo(owner);
        pack();
        setVisible(true);
    }

    public String getConfirm() {
        return confirm;
    }
}
