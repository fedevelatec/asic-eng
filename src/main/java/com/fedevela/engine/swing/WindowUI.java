package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import com.fedevela.asic.util.TypeCast;
import com.fedevela.engine.ConfigureEngine;
import com.fedevela.engine.SwingEngine;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class WindowUI extends javax.swing.JDialog {

    public enum WindowResult {

        NONE,
        OK,
        CANCEL,
        CLOSE
    };
    private static final Integer _defaultWidth = 240;
    private static final Integer _defaultHeight = 100;
    private javax.swing.JScrollPane _scroll;
    private javax.swing.JPanel _container;
    private javax.swing.JButton _btnOk;
    private javax.swing.JButton _btnCancel;
    private ConfigureEngine _ce;
    private javax.swing.GroupLayout _layout;
    private WindowResult _windowResult = WindowResult.NONE;
    private java.awt.Component _owner;
    //
    private SwingEngine _engine;
    //
    private Short doccod;

    private void _initialize() {
        _ce = _ce == null ? new ConfigureEngine() : _ce;
        _ce.setWidth(_ce.getWidth() != null ? _ce.getWidth() : _defaultWidth);
        _ce.setHeight(_ce.getHeight() != null ? _ce.getHeight() : _defaultHeight);
        if (TypeCast.isBlank(_ce.getTitle())) {
            _ce.setTitle("Window UI");
        }
        /* Init JDialog */
        _owner = _owner == null ? new javax.swing.JFrame() : _owner;
        setTitle(_ce.getTitle());
        setModal(_ce.isModal() == null ? false : _ce.isModal());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _windowResult = WindowResult.CANCEL;
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        /* Init button ok */
        _btnOk = new javax.swing.JButton("Aceptar");
        _btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adeamx/adeadms/images/accept16x16.png")));
        _btnOk.setActionCommand("Ok");
        _btnOk.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _windowResult = WindowResult.OK;
                dispose();
            }
        });
        /* Init button cancel */
        _btnCancel = new javax.swing.JButton("Cancelar");
        _btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adeamx/adeadms/images/cancel16x16.png")));
        _btnCancel.setActionCommand("Cancel");
        _btnCancel.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _windowResult = WindowResult.CANCEL;
                dispose();
            }
        });
        /* */
        _container = new javax.swing.JPanel();
        _container.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        _layout = new javax.swing.GroupLayout(_container);
        _container.setLayout(_layout);
        _layout.setHorizontalGroup(
                _layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE));
        _layout.setVerticalGroup(
                _layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 47, Short.MAX_VALUE));
        _scroll = new javax.swing.JScrollPane();
        _scroll.setViewportView(_container);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(_scroll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(0, 58, Short.MAX_VALUE)
                                                .addComponent(_btnOk)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(_btnCancel)))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(_scroll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(_btnOk)
                                        .addComponent(_btnCancel))
                                .addContainerGap()));
        /* Init Window*/
        getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        setResizable(false);
        setPreferredSize(new Dimension(_ce.getWidth(), _ce.getHeight()));
        setSize(getPreferredSize());
        pack();
    }

    public WindowUI(ConfigureEngine _ce, java.awt.Component _owner) {
        super();
        this._ce = _ce;
        this._owner = _owner;
        _initialize();
    }

    public WindowUI(ConfigureEngine _ce) {
        this(_ce, null);
    }

    public SwingEngine getEngine() {
        return _engine;
    }

    public void setEngine(SwingEngine engine) {
        this._engine = engine;
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            setLocationRelativeTo(_container);
            _windowResult = WindowResult.NONE;
        }
        super.setVisible(b);
    }

    public Short getDoccod() {
        return doccod;
    }

    public void setDoccod(Short doccod) {
        this.doccod = doccod;
    }

    public JPanel getContainer() {
        return _container;
    }

    public WindowResult getWindowResult() {
        return _windowResult;
    }
}
