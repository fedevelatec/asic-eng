package com.fedevela.engine;

/**
 * Created by fvelazquez on 11/04/14.
 */
import com.fedevela.core.asic.definition.pojos.AdeamxDefinition;
import com.fedevela.asic.util.TypeCast;
import com.fedevela.engine.swing.*;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.codicentro.cliser.dao.CliserDao;
import net.codicentro.core.Utils;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwingEngine implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final TypeCast cast = new TypeCast();
    private final Logger logger = LoggerFactory.getLogger(SwingEngine.class);

    private enum TEnvironment {

        COMPONENT,
        POPUP
    };
    /**
     * Lista para almacenar cualquier instancia propia de manera única.
     *
     * Se modificó el nombre de la variable porque existe la necesidad de que se
     * puedan almacenar instancias únicas de cualquier tipo de objetos.
     */
    private Map<String, Object> _instances;
    private Map<String, Object> _globalInstances;
    private java.awt.Container _container;
    private java.awt.Component _owner;
    private CliserDao _dao;
    private Integer _left;
    private Integer _top;
    // Last width
    private Integer _width;
    // Last max height
    private Integer _height;
    private static ScriptEngine SE = new ScriptEngineManager().getEngineByName("JavaScript");

    private void _initialize() {
        _left = 10;
        _top = 10;
        _width = 0;
        _height = 0;
        _instances = new HashMap<String, Object>();
    }

    public SwingEngine(java.awt.Component _owner, java.awt.Container _container) {
        this._owner = _owner;
        this._container = _container;
        _initialize();
    }

    public SwingEngine(java.awt.Component _owner, java.awt.Container _container, Integer pointY) {
        this(_owner, _container);
        this._top = pointY;
    }

    public SwingEngine(java.awt.Component _owner, java.awt.Container _container, CliserDao _dao) {
        this(_owner, _container);
        this._dao = _dao;
    }

    public SwingEngine(java.awt.Component _owner, java.awt.Container _container, CliserDao _dao, Integer pointY) {
        this(_owner, _container, _dao);
        this._top = pointY;
    }

    public SwingEngine(java.awt.Component _owner, java.awt.Container _container, CliserDao _dao, Map<String, Object> _globalInstances) {
        this(_owner, _container, _dao);
        this._globalInstances = _globalInstances;
    }

    public void changedContainer(java.awt.Container container) {
        changedContainer(container, _left, 10);
    }

    public void changedContainer(java.awt.Container container, Integer _left, Integer _top) {
        this._container = container;
        this._left = _left;
        this._top = _top;
        _width = 0;
        _height = 0;
    }

    private void set(Object cmp, String property, Object value) {
        /**
         *
         */
        java.lang.reflect.Method m = null;
        String methodName = null;
        for (Method method : cmp.getClass().getMethods()) {
            if (method.getName().equals(property)) {
                m = method;
                methodName = property;
                break;
            }
        }
        try {
            if (m == null) {
                methodName = "set" + TypeCast.toFirtUpperCase(property);
                m = TypeCast.getMethod(cmp.getClass(), methodName, value == null ? Object.class : value.getClass());
            }
            if (m.getParameterTypes() != null && m.getParameterTypes().length == 1) {
                m.invoke(cmp, value);
            } else {
                m.invoke(cmp);
            }
        } catch (Exception ex) {
            try {
                /**
                 * Estas condiciones es para resolver el problema de los tipos
                 * primitivos.
                 *
                 */
                if (value instanceof java.lang.Boolean) {
                    m = TypeCast.getMethod(cmp.getClass(), methodName, Boolean.TYPE);
                } else if (value instanceof java.lang.Character) {
                    m = TypeCast.getMethod(cmp.getClass(), methodName, Character.TYPE);
                } else if (value instanceof java.lang.Byte) {
                    m = TypeCast.getMethod(cmp.getClass(), methodName, Byte.TYPE);
                } else if (value instanceof java.lang.Short) {
                    m = TypeCast.getMethod(cmp.getClass(), methodName, Short.TYPE);
                } else if (value instanceof java.lang.Integer) {
                    m = TypeCast.getMethod(cmp.getClass(), methodName, Integer.TYPE);
                } else if (value instanceof java.lang.Long) {
                    m = TypeCast.getMethod(cmp.getClass(), methodName, Long.TYPE);
                } else if (value instanceof java.lang.Float) {
                    m = TypeCast.getMethod(cmp.getClass(), methodName, Float.TYPE);
                } else if (value instanceof java.lang.Double) {
                    m = TypeCast.getMethod(cmp.getClass(), methodName, Double.TYPE);
                }
                if (m != null) {
                    if (m.getParameterTypes() != null && m.getParameterTypes().length == 1) {
                        m.invoke(cmp, value);
                    } else {
                        m.invoke(cmp);
                    }
                } else {
                    throw new EngineException("Method not found: " + property);
                }
            } catch (Exception ex1) {
                logger.error(ex.getMessage(), ex1);
            }
        }
    }

    private void onResult(NativeArray result) throws EngineException {
        if (result != null) {
            for (int idx = 0; idx < result.getLength(); idx++) {
                NativeObject obj = (NativeObject) result.get(idx, null);
                if (obj != null) {
                    String name = TypeCast.toString(obj.get("name", null));
                    for (Object ids : obj.getIds()) {
                        String property = TypeCast.toString(ids);
                        if (!property.equals("name")) {
                            Object cmp = _instances.get(name);
                            if (cmp == null) {
                                throw new EngineException("No component with name " + name + ".");
                            }
                            set(cmp, property, obj.get(property, null));
                        }
                    }
                }
            }
        }
    }

    private Object getService(Service srv) throws EngineException {
        Object bn;
        Map<String, Object> nst = !TypeCast.isBlank(srv.getScope()) && srv.getScope().equals("global") ? _globalInstances : _instances;
        if (!TypeCast.isBlank(srv.getName()) && nst.containsKey(srv.getName())) {
            // Caso para cuando se define el nombre de la instancia
            bn = nst.get(srv.getName());
        } else if (!TypeCast.isBlank(srv.getInterface()) && nst.containsKey(srv.getInterface())) {
            // Caso para encontrar el nombre de la instancia por el nombre de su clase y/o interface
            bn = nst.get(srv.getInterface());
        } else {
            throw new EngineException("Servicio no encontrado " + srv.getId() + ". Referencia " + srv.toString());
        }
        return bn;
    }

    private void onAction(JComponent source, final Listener listener) {
        try {
            SE = SE.getFactory().getScriptEngine();
            if (source instanceof FieldUI) {
                SE.put("isUserValid", ((FieldUI) source).isUserValid());
                evalInputField((FieldUI) source, listener.getScript(), false);
                if (!((FieldUI) source).isUserValid()) {
                    return;
                }
            } else if (source instanceof GridUI) {
                SE.put("rowSelected", ((GridUI) source).getSelected());
            }
            if (SE != null && listener.getService() != null && !listener.getService().isEmpty()) {
                for (Service srv : listener.getService()) {
                    Object bn = getService(srv);
                    java.lang.reflect.Method m = bn.getClass().getMethod(srv.getMethod(), srv.getParameterTypes());
                    if (m.getParameterTypes() != null) {
                        List values = new ArrayList();
                        Integer idx = 0;
                        for (Class<?> c : m.getParameterTypes()) {
                            String value = srv.getParams().getParam().get(idx).getValue();
                            String _scope = srv.getParams().getParam().get(idx).getScope();
                            if (TypeCast.isBlank(value) || value.trim().toLowerCase().equals("null")) {
                                // Valor nulo ó vacio
                                values.add(null);
                            } else if (value.startsWith("[") && value.endsWith("]")) {
                                value = value.substring(1, value.length() - 1);
                                String[] arrParams = value.split(",");
                                List lstParams = new ArrayList();
                                for (String strParam : arrParams) {
                                    if (strParam.startsWith("{") && strParam.endsWith("}")) {
                                        // Referencia a un objeto
                                        strParam = strParam.substring(1, strParam.length() - 1);
                                        if (!TypeCast.isBlank(strParam)) {
                                            String[] _split = strParam.split("\\.");
                                            Object _ref = _split[0].equals("this") ? source : TypeCast.isBlank(_scope) ? _instances.get(_split[0]) : _globalInstances.get(_split[0]);
                                            if (_split.length == 1) {
                                                lstParams.add(_ref);
                                            } else {
                                                lstParams.add(_ref.getClass().getMethod(_split[1]).invoke(_ref));
                                            }
                                        } else {
                                            lstParams.add(null);
                                        }
                                    }
                                }
                                values.add(lstParams);
                            } else if (value.startsWith("{") && value.endsWith("}")) {
                                // Referencia a un objeto
                                value = value.substring(1, value.length() - 1);
                                if (!TypeCast.isBlank(value)) {
                                    String[] _split = value.split("\\.");
                                    Object _ref = _split[0].equals("this") ? source : TypeCast.isBlank(_scope) ? _instances.get(_split[0]) : _globalInstances.get(_split[0]);
                                    if (_split.length == 1) {
                                        values.add(_ref);
                                    } else {
                                        values.add(_ref.getClass().getMethod(_split[1]).invoke(_ref));
                                    }
                                } else {
                                    values.add(null);
                                }
                            } else {
                                if (!c.getSimpleName().equals(value.getClass().getSimpleName())) {
                                    values.add(TypeCast.class.getMethod("to" + c.getSimpleName(), value.getClass()).invoke(cast, value));
                                } else {
                                    values.add(value);
                                }
                            }
                            idx++;
                        }
                        SE.put(srv.getId(), m.invoke(bn, values.toArray()));
                    } else {
                        SE.put(srv.getId(), m.invoke(bn));
                    }
                }
            }
            if (TypeCast.isBlank(listener.getScript())) {
                logger.warn("Not script found.");
            } else {
                SE.eval(listener.getScript());
                onResult((NativeArray) SE.get("result"));
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void onItemStateChanged(ItemEvent evt, String script) {
        try {
            SE = SE.getFactory().getScriptEngine();
            CheckBoxUI chk = (CheckBoxUI) evt.getItem();
            SE.put("isSelected", chk.isSelected());
            SE.eval(script);
            onResult((NativeArray) SE.get("result"));
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void evalInputField(final FieldUI in, final String script, Boolean checkValidate) throws ScriptException, EngineException {
        if (!TypeCast.isBlank(script)) {
            SE.put("text", in.getText());
            SE.put("isUserValid", in.isUserValid());
            SE.eval(script);
            String isValid = (String) SE.get("isValid");
            if (checkValidate) {
                in.setUserValid(isValid);
            }
            // Independientemente de que sea valida o no la condicion
            // podemos ejecutar una accion en los componentes
            if (SE.get("result") != null) {
                onResult((NativeArray) SE.get("result"));
            }
        }
    }

    private Boolean onChangedInputField(final FieldUI fld, final String script, final List<Service> services) {
        SE = SE.getFactory().getScriptEngine();
        try {
            if (!fld.isFocus()) {
                evalInputField(fld, script, false);
                return true;
            }
            if (TypeCast.isBlank(fld.getText()) && fld.isAllowBlank()) {
                evalInputField(fld, script, false);
                return true;
            }
            fld.checkValidate();
            if (!fld.isUserValid()) {
                evalInputField(fld, script, false);
                return true;
            }
            if (fld.isDoubleTyping()) {
                fld.setVisible(false);
                DoubleTypingUI d = new DoubleTypingUI(_owner, fld);
                fld.setVisible(true);
                fld.setUserValid(d.getConfirm());
                if (d.getConfirm() != null) {
                    fld.setText(null);
                    evalInputField(fld, script, false);
                    return true;
                }
                if (!fld.isUserValid()) {
                    evalInputField(fld, script, false);
                    return true;
                }
            }
            if (SE != null && services != null && !services.isEmpty()) {
                for (Service srv : services) {
                    Object bn;
                    try {
                        bn = getService(srv);
                    } catch (EngineException ex) {
                        fld.setUserValid(ex.getMessage());
                        evalInputField(fld, script, false);
                        return true;
                    }
                    java.lang.reflect.Method m = bn.getClass().getMethod(srv.getMethod(), srv.getParameterTypes());
                    if (m.getParameterTypes() != null) {
                        List values = new ArrayList();
                        Integer idx = 0;
                        for (Class<?> c : m.getParameterTypes()) {
                            String value = srv.getParams().getParam().get(idx).getValue();
                            if (TypeCast.isBlank(value) || value.trim().toLowerCase().equals("null")) {
                                // Valor nulo ó vacio
                                values.add(null);
                            } else if (value.startsWith("{") && value.endsWith("}")) {
                                // Referencia a un objeto
                                value = value.substring(1, value.length() - 1);
                                if (!TypeCast.isBlank(value)) {
                                    String[] _split = value.split("\\.");
                                    Object _ref = _split[0].equals("this") ? fld : _instances.get(_split[0]);
                                    if (_split.length == 1) {
                                        values.add(_ref);
                                    } else {
                                        values.add(_ref.getClass().getMethod(_split[1]).invoke(_ref));
                                    }
                                } else {
                                    values.add(null);
                                }
                            } else if (value.startsWith("[") && value.endsWith("]")) {
                                value = value.substring(1, value.length() - 1);
                                String[] arrParams = value.split(",");
                                List lstParams = new ArrayList();
                                for (String strParam : arrParams) {
                                    if (strParam.startsWith("{") && strParam.endsWith("}")) {
                                        // Referencia a un objeto
                                        strParam = strParam.substring(1, strParam.length() - 1);
                                        if (!TypeCast.isBlank(strParam)) {
                                            String[] _split = strParam.split("\\.");
                                            Object _ref = _split[0].equals("this") ? fld : _instances.get(_split[0]);
                                            if (_split.length == 1) {
                                                lstParams.add(_ref);
                                            } else {
                                                lstParams.add(_ref.getClass().getMethod(_split[1]).invoke(_ref));
                                            }
                                        } else {
                                            lstParams.add(null);
                                        }
                                    }
                                }
                                values.add(lstParams);
                            } else {
                                if (!c.getSimpleName().equals(value.getClass().getSimpleName())) {
                                    values.add(TypeCast.class.getMethod("to" + c.getSimpleName(), value.getClass()).invoke(cast, value));
                                } else {
                                    values.add(value);
                                }
                            }
                            idx++;
                        }
                        SE.put(srv.getId(), m.invoke(bn, values.toArray()));
                    } else {
                        SE.put(srv.getId(), m.invoke(bn));
                    }
                }
            }
            evalInputField(fld, script, true);
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            fld.setUserValid("Error: " + ex.getMessage());
            return true;
        }
    }

    private void onEnterListenerInputField(final JComponent c) {
        c.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    c.transferFocus();
                }
            }
        });
    }

    /**
     *
     * @param c
     */
    private void updateSizeLocation(Component c) {
        // SIZE
        _width = _width.compareTo(c.getPreferredSize().width) >= 0 ? _width : c.getPreferredSize().width;
        _height = _height.compareTo(c.getPreferredSize().height) >= 0 ? _height : c.getPreferredSize().height;
        // LOCATION
        _left += c.getWidth();
    }

    private void render(AdeamxDefinition definition) {
        try {
            ConfigureEngine conf;
            if (TypeCast.isBlank(definition.getConfigure())) {
                conf = new ConfigureEngine();
            } else {
                conf = Utils.convertToEntity(definition.getConfigure(), ConfigureEngine.class);
            }
            if (!TypeCast.isBlank(conf.getType())) {
                if (conf.isBr() != null && conf.isBr()) {
                    _left = 10;
                    _width = 0;
                    _top += conf.getTop() == null ? _height : conf.getTop();
                    _height = 0;
                } else if (conf.getTop() != null) {
                    _top = conf.getTop();
                }
                _left = conf.getLeft() == null ? _left : conf.getLeft();
                if (conf.getType().equals("CheckBox")) {
                    CheckBoxUI chk = new CheckBoxUI(conf);
                    chk.setKey(definition.getFeature());
                    chk.setName(TypeCast.isBlank(conf.getName()) ? "chk" + definition.getId() : conf.getName());
                    if (_instances.containsKey(chk.getName())) {
                        throw new Exception("Duplicate components name " + chk.getName() + " into definition ID " + definition.getId());
                    }
                    chk.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
                    chk.setLocation(_left, _top);
                    chk.setEnabled(!conf.isDisabled());
                    chk.setVisible(conf.isVisible());
                    if (conf.getListeners() != null) {
                        for (final Listener listener : conf.getListeners().getListeners()) {
                            if (TypeCast.isBlank(listener.getType())) {
                                throw new RuntimeException("Attribute type is required into listener element for definition id " + definition.getId() + ".");
                            }
                            if (listener.getType().equals("onChanged")) {
                                chk.addItemListener(new ItemListener() {
                                    @Override
                                    public void itemStateChanged(ItemEvent e) {
                                        onItemStateChanged(e, listener.getScript());
                                    }
                                });
                            } else {
                                throw new UnsupportedOperationException("Listener " + listener.getType() + " not supported yet for definition id " + definition.getId() + ".");
                            }
                        }
                    }
                    updateSizeLocation(chk);
                    _container.add(chk);
                    _instances.put(chk.getName(), chk);
                } else if (conf.getType().equals("Button")) {
                    final ButtonUI btn = new ButtonUI(conf.getText());
                    btn.setName(TypeCast.isBlank(conf.getName()) ? "btn" + definition.getId() : conf.getName());
                    if (_instances.containsKey(btn.getName())) {
                        throw new Exception("Duplicate components name " + btn.getName() + " into definition ID " + definition.getId());
                    }
                    btn.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
                    btn.setSize(conf.getWidth(), conf.getHeight());
                    btn.setLocation(_left, _top);
                    btn.setEnabled(!conf.isDisabled());
                    btn.setVisible(conf.isVisible());
                    if (!TypeCast.isBlank(conf.getIcon())) {
                        btn.setIcon(new javax.swing.ImageIcon(getClass().getResource(conf.getIcon())));
                    }
                    if (conf.getListeners() != null) {
                        for (final Listener listener : conf.getListeners().getListeners()) {
                            if (TypeCast.isBlank(listener.getType())) {
                                throw new RuntimeException("Attribute type is required into listener element for definition id " + definition.getId() + ".");
                            }
                            if (listener.getType().equals("onClick")) {
                                btn.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent evt) {
                                        onAction(btn, listener);
                                    }
                                });
                            } else {
                                throw new UnsupportedOperationException("Listener " + listener.getType() + " not supported yet for definition id " + definition.getId() + ".");
                            }
                        }
                    }
                    updateSizeLocation(btn);
                    _container.add(btn);
                    _instances.put(btn.getName(), btn);
                } else if (conf.getType().equals("ComboBox")) {
                    final ComboBoxUI cmb = new ComboBoxUI(conf);
                    cmb.setName(TypeCast.isBlank(conf.getName()) ? "cmb" + definition.getId() : conf.getName());
                    if (_instances.containsKey(cmb.getName())) {
                        throw new Exception("Duplicate components name " + cmb.getName() + " into definition ID " + definition.getId());
                    }
                    Object[] items = null;
                    if (conf.getItem() != null) {
                        try {
                            switch (conf.getItem().getType()) {
                                case SQL:
                                    items = conf.getItem().getValues(_dao);
                                    break;
                                case REFERENCE_CMP:
                                    items = conf.getItem().getValues((ComboBoxUI) _instances.get(conf.getItem().getValue()));
                                    break;
                                case FIXED_ARRAY:
                                    if (TypeCast.isBlank(conf.getItem().getId())) {
                                        items = conf.getItem().getValues();
                                    } else {
                                        items = !TypeCast.isBlank(conf.getItem().getScope()) && conf.getItem().getScope().equals("global") ? conf.getItem().getValues(_globalInstances.get(conf.getItem().getId())) : conf.getItem().getValues(_instances.get(conf.getItem().getId()));
                                    }
                                    break;
                            }
                        } catch (EngineException ex) {
                            logger.error("Ha ocurrido un problema en el componente " + cmb.getName() + ".", ex);
                        }
                    }
                    cmb.setItem(items);
                    cmb.setLocation(_left, _top);
                    cmb.setEnabled(!conf.isDisabled());
                    cmb.setSelectedItem(null);
                    cmb.setVisible(conf.isVisible());
                    if (conf.getListeners() != null) {
                        for (final Listener listener : conf.getListeners().getListeners()) {
                            if (listener.getType().equals("onChanged")) {
                                cmb.addItemListener(new java.awt.event.ItemListener() {
                                    @Override
                                    public void itemStateChanged(java.awt.event.ItemEvent evt) {
                                        onAction(cmb, listener);
                                    }
                                });
                            }
                        }
                    }
                    updateSizeLocation(cmb);
                    _container.add(cmb);
                    _instances.put(cmb.getName(), cmb);
                } else if (conf.getType().equals("NumberField")
                        || conf.getType().equals("TextField")
                        || conf.getType().equals("DateField")) {

                    final FieldUI ifl;
                    if (conf.getType().equals("NumberField")) {
                        ifl = new NumberFieldUI(conf);
                        ifl.setName(TypeCast.isBlank(conf.getName()) ? "nmb" + definition.getId() : conf.getName());
                    } else if (conf.getType().equals("DateField")) {
                        ifl = new DateFieldUI(conf);
                        ifl.setName(TypeCast.isBlank(conf.getName()) ? "dte" + definition.getId() : conf.getName());
                    } else {
                        ifl = new TextFieldUI(conf);
                        ifl.setName(TypeCast.isBlank(conf.getName()) ? "txt" + definition.getId() : conf.getName());
                    }
                    if (_instances.containsKey(ifl.getName())) {
                        throw new Exception("Duplicate components name " + ifl.getName() + " into definition ID " + definition.getId());
                    }
                    ifl.setDoccod(conf.getDoccod());

                    logger.info("" + ifl.getPreferredSize().height);
                    ifl.setLocation(_left, _top);
                    if (!conf.getType().equals("DateField")) {
                        ifl.setMaxLength(conf.getMaxLength());
                    }
                    ifl.setCaseSensitive(conf.getCaseSensitive());
                    ifl.setMinLength(conf.getMinLength());
                    ifl.setAllowBlank(conf.isAllowBlank());
                    ifl.setDoubleTyping(conf.isDoubleTyping());
                    ifl.setVisible(conf.isVisible());

                    if (conf.getFontSize() != null) {
                        ifl.setFont(new Font(ifl.getFont().getName(), ifl.getFont().getStyle(), conf.getFontSize()));
                    }
                    if (conf.getDoubleTypingEnter() != null) {
                        ifl.setDoubleTypingEnter(conf.getDoubleTypingEnter());
                        if (conf.getDoubleTypingEnter()) {
                            ifl.setToolTipText("Doble type");
                            ifl.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
                            ifl.setFocusTraversalKeysEnabled(false);
                            ifl.addKeyListener(new java.awt.event.KeyAdapter() {
                                @Override
                                public void keyReleased(KeyEvent e) {
                                    if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_TAB) {
                                        final FieldUI in = (FieldUI) ifl;
                                        if (!ifl.isUserValid()) {
                                            ifl.setVisible(false);
                                            DoubleTypingUI d = new DoubleTypingUI(_owner, in);
                                            ifl.setVisible(true);
                                            ifl.setUserValid(d.getConfirm());
                                            if (d.getConfirm() != null) {
                                                ifl.requestFocus();
                                                ifl.requestFocusInWindow();
                                                ifl.setText(null);
                                                ifl.setValue(null);
                                            } else {
                                                ifl.checkValidate();
                                                ifl.requestFocus();
                                                ifl.requestFocusInWindow();
                                                ifl.transferFocus();
                                            }

                                        }
                                    }
                                }

                                @Override
                                public void keyTyped(KeyEvent e) {
                                    if (ifl.getText().length() == 0) {
                                        ifl.setUserValid("Error debe insertar un valor");
                                    }
                                    if (ifl.getText().length() > 0) {
                                        ifl.setUserValid("Error");
                                    }
                                }
                            });
                        }
                    } else {
                        if (conf.getListeners() != null) {
                            for (final Listener listener : conf.getListeners().getListeners()) {
                                if (TypeCast.isBlank(listener.getType())) {
                                    throw new RuntimeException("Attribute type is required into listener element for definition id " + definition.getId() + ".");
                                }
                                if (listener.getType().equals("onChanged")) {
                                    ifl.setInputVerifier(new InputVerifier() {
                                        @Override
                                        public boolean verify(JComponent input) {
                                            return onChangedInputField(ifl, listener.getScript(), listener.getService());
                                        }
                                    });
                                    if (!TypeCast.isBlank(listener.getKeyMap())) {
                                        String[] _keys = listener.getKeyMap().split(",");
                                        for (String _key : _keys) {
                                            if (_key.equals("ENTER")) {
                                                ifl.addKeyListener(new java.awt.event.KeyAdapter() {
                                                    @Override
                                                    public void keyReleased(KeyEvent e) {
                                                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                                            ifl.transferFocus();
                                                            onChangedInputField(ifl, listener.getScript(), listener.getService());
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                } else if (listener.getType().equals("onKeyEnter")) {
                                    ifl.addKeyListener(new java.awt.event.KeyAdapter() {
                                        @Override
                                        public void keyReleased(KeyEvent e) {
                                            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                                onAction(ifl, listener);
                                            }
                                        }
                                    });
                                } else if (listener.getType().equals("onEnterListener")) {
                                    onEnterListenerInputField(ifl);
                                } else {
                                    throw new UnsupportedOperationException("Listener " + listener.getType() + " not supported yet for definition id " + definition.getId() + ".");
                                }

                            }
                        } else {
                            ifl.setInputVerifier(new InputVerifier() {
                                @Override
                                public boolean verify(JComponent input) {
                                    return onChangedInputField(ifl, null, null);
                                }
                            });
                        }
                    }
                    updateSizeLocation(ifl);
                    _container.add(ifl);
                    _instances.put(ifl.getName(), ifl);
                } else if (conf.getType().equals("TextAreaField")) {
                    /* final TextAreaFieldUI ifl = new TextAreaFieldUI(conf);
                     ifl.setName(TypeCast.isBlank(conf.getName()) ? "txa" + definition.getId() : conf.getName());
                     if (_instances.containsKey(ifl.getName())) {
                     throw new Exception("Duplicate components name " + ifl.getName() + " into definition ID " + definition.getId());
                     }
                     ifl.setDoccod(conf.getDoccod());
                     logger.info("" + ifl.getPreferredSize().height);
                     ifl.setLocation(_left, _top);
                     ifl.setEnabled(!conf.isDisabled());
                     if (!conf.getType().equals("DateField")) {
                     ifl.setMaxLength(conf.getMaxLength());
                     }
                     ifl.setCaseSensitive(conf.getCaseSensitive());
                     ifl.setMinLength(conf.getMinLength());
                     ifl.setAllowBlank(conf.isAllowBlank());
                     ifl.setDoubleTyping(conf.isDoubleTyping());
                     ifl.setVisible(conf.isVisible());

                     if (conf.getFontSize() != null) {
                     ifl.setFont(new Font(ifl.getFont().getName(), ifl.getFont().getStyle(), conf.getFontSize()));
                     }
                     updateSizeLocation(ifl);
                     _container.add(ifl);
                     _instances.put(ifl.getName(), ifl);*/
                } else if (conf.getType().equals("Window")) {
                    WindowUI wnd = new WindowUI(conf, _owner);// ,
                    // owner,
                    wnd.setName(TypeCast.isBlank(conf.getName()) ? "wnd" + definition.getId() : conf.getName());
                    if (_instances.containsKey(wnd.getName())) {
                        throw new Exception("Duplicate components name " + wnd.getName() + " into definition ID " + definition.getId());
                    }
                    wnd.setDoccod(conf.getDoccod().shortValue());
//                    JScrollPane jsp = new JScrollPane();
//                    JPanel jp = new JPanel();
//                    jp.setLayout(null);
//                    jsp.setViewportView(jp);
//                    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(ppp.getContentPane());
//                    ppp.getContentPane().setLayout(layout);
//                    layout.setHorizontalGroup(
//                            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jsp, javax.swing.GroupLayout.DEFAULT_SIZE, conf.getWidth(), Short.MAX_VALUE).addContainerGap()));
//                    layout.setVerticalGroup(
//                            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jsp, javax.swing.GroupLayout.DEFAULT_SIZE, conf.getHeight(), Short.MAX_VALUE).addContainerGap()));
//                    ppp.pack();
                    SwingEngine se = new SwingEngine(wnd, wnd.getContainer(), _dao, _globalInstances);
                    se.render(definition.getAdeamxDefinitionList());
                    wnd.setEngine(se);
                    _instances.put(wnd.getName(), wnd);
                } else if (conf.getType().equals("Label")) {
                    LabelUI lbl = new LabelUI(conf);
                    lbl.setName(TypeCast.isBlank(conf.getName()) ? "lbl" + definition.getId() : conf.getName());
                    lbl.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
                    lbl.setLocation(_left, _top);
                    lbl.setEnabled(!conf.isDisabled());
                    lbl.setVisible(conf.isVisible());
                    if (_instances.containsKey(lbl.getName())) {
                        throw new EngineException("Duplicate components name " + lbl.getName() + " into definition ID " + definition.getId());
                    }
                    updateSizeLocation(lbl);
                    _container.add(lbl);
                    _instances.put(lbl.getName(), lbl);
                } else if (conf.getType().equals("Grid")) {
                    final GridUI tbl = new GridUI(conf);
                    tbl.setName(TypeCast.isBlank(conf.getName()) ? "tbl" + definition.getId() : conf.getName());
                    if (_instances.containsKey(tbl.getName())) {
                        throw new Exception("Duplicate components name " + tbl.getName() + " into definition ID " + definition.getId());
                    }
                    tbl.setLocation(_left, _top);
                    tbl.setEnabled(!conf.isDisabled());
                    tbl.setVisible(conf.isVisible());
                    if (conf.getListeners() != null) {
                        for (final Listener listener : conf.getListeners().getListeners()) {
                            if (TypeCast.isBlank(listener.getType())) {
                                throw new RuntimeException("Attribute type is required into listener element for definition id " + definition.getId() + ".");
                            }
                            if (listener.getType().equals("onClick") && listener.getName().equals("btnAdd")) {
                                tbl.addBtnAddAction(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent evt) {
                                        onAction(tbl, listener);
                                    }
                                });
                            } else if (listener.getType().equals("onClick") && listener.getName().equals("btnEdit")) {
                                tbl.addBtnEditAction(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent evt) {
                                        onAction(tbl, listener);
                                    }
                                });
                            } else if (listener.getType().equals("onClick") && listener.getName().equals("btnRemove")) {
                                tbl.addBtnRemoveAction(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent evt) {
                                        onAction(tbl, listener);
                                    }
                                });
                            } else if (listener.getType().equals("onRowSelected")) {
                                tbl.addRowSelectedAction(new ListSelectionListener() {
                                    @Override
                                    public void valueChanged(ListSelectionEvent lse) {
                                        onAction(tbl, listener);
                                    }
                                });
                            } else {
                                throw new UnsupportedOperationException("Listener " + listener.getType() + " not supported yet for definition id " + definition.getId() + ".");
                            }
                        }
                    }
                    updateSizeLocation(tbl);
                    _container.add(tbl);
                    _instances.put(tbl.getName(), tbl);
                } else {
                    Component cmp = (Component) Class.forName(conf.getType()).newInstance();
                    cmp.setName(TypeCast.isBlank(conf.getName()) ? "cmp" + definition.getId() : conf.getName());
                    cmp.setSize(conf.getWidth(), conf.getHeight());
                    cmp.setLocation(_left, _top);
                    cmp.setEnabled(!conf.isDisabled());
                    if (_instances.containsKey(cmp.getName())) {
                        throw new Exception("Duplicate components name " + cmp.getName() + " into definition ID " + definition.getId());
                    }
                    updateSizeLocation(cmp);
                    _container.add(cmp);
                    _instances.put(cmp.getName(), cmp);
                }
                if (!conf.getType().equals("Window")) {
//                    if (conf.getNewLine() != null) {
//                        _left = 10;
//                        _top += conf.getNewLine();
//                    } else {
//                        _left += conf.getWidth() + 10;
//                    }

                    if (_container.getSize().width < _left) {
                        _container.setPreferredSize(new Dimension(_left, _container.getPreferredSize().height));
                    }
                    if (_container.getSize().height < _top + _height) {
                        _container.setPreferredSize(new Dimension(_container.getPreferredSize().width, _top + _height + 10));
                    }
                    if (definition.getAdeamxDefinitionList() != null && !definition.getAdeamxDefinitionList().isEmpty()) {
                        Collections.sort(definition.getAdeamxDefinitionList());
                        for (AdeamxDefinition adeamxDefinition : definition.getAdeamxDefinitionList()) {
                            render(adeamxDefinition);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public void render(List<AdeamxDefinition> definitions) {
        Collections.sort(definitions);
        for (AdeamxDefinition definition : definitions) {
            render(definition);
        }
    }

    public Component getComponent(final String name) {
        return (Component) _instances.get(name);
    }

    public Map<String, Object> getInstances() {
        return _instances;
    }

    public void setValue(String name, Object value) {
        Object cmp = getComponent(name);
        if (cmp instanceof CheckBoxUI) {
            ((CheckBoxUI) cmp).setSelected(TypeCast.toBoolean(value));
        } else if (cmp instanceof FieldUI) {
            ((FieldUI) cmp).setText(TypeCast.toString(value));
        } else if (cmp instanceof ComboBoxUI) {
            ((ComboBoxUI) cmp).setSelectedItem(value);
        }
    }

    /**
     * Reset all values component.
     */
    public void reset() {
        for (Object cmp : _instances.values()) {
            if (cmp instanceof CheckBoxUI) {
                ((CheckBoxUI) cmp).setSelected(false);
            } else if (cmp instanceof ComboBoxUI) {
                ((ComboBoxUI) cmp).setSelectedItem(null);
            } else if (cmp instanceof FieldUI) {
                ((FieldUI) cmp).setText(null);
                ((FieldUI) cmp).setUserValid(null);
            } else if (cmp instanceof WindowUI) {
                ((WindowUI) cmp).getEngine().reset();
            }
        }
    }

    /**
     * Initialized
     */
    public void clear() {
        _initialize();
    }

    public void addGlobalInstance(String key, Object value) {
        _globalInstances = _globalInstances == null ? new HashMap<String, Object>() : _globalInstances;
        if (_globalInstances.containsKey(key)) {
            logger.warn("The " + key + " global instance exists, its value is overwritten.");
        }
        _globalInstances.put(key, value);
    }

    public void addGlobalInstance(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        String key;
        if (value.getClass().isAnnotationPresent(org.springframework.stereotype.Service.class) && !TypeCast.isBlank(value.getClass().getAnnotation(org.springframework.stereotype.Service.class).value())) {
            key = value.getClass().getAnnotation(org.springframework.stereotype.Service.class).value();
        } else if (value.getClass().getInterfaces() != null && value.getClass().getInterfaces().length > 0) {
            key = value.getClass().getInterfaces()[0].getName();
        } else {
            key = value.getClass().getName();
        }
        addGlobalInstance(key, value);
    }

    public void addInstance(String key, Object value) {
        if (_instances.containsKey(key)) {
            logger.warn("The " + key + " instance exists, its value is overwritten.");
        }
        _instances.put(key, value);
    }

    public void addInstance(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        String key;
        if (value.getClass().isAnnotationPresent(org.springframework.stereotype.Service.class) && !TypeCast.isBlank(value.getClass().getAnnotation(org.springframework.stereotype.Service.class).value())) {
            key = value.getClass().getAnnotation(org.springframework.stereotype.Service.class).value();
        } else if (value.getClass().getInterfaces() != null && value.getClass().getInterfaces().length > 0) {
            key = value.getClass().getInterfaces()[0].getName();
        } else {
            key = value.getClass().getName();
        }
        addInstance(key, value);
    }
}
