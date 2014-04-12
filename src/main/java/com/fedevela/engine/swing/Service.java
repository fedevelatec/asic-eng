package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Service implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    // Bean name
    private String name;
    private String _interface;
    private String _method;
    private ServiceParams params;
    private Class<?>[] paramTypes;
    private String _scope;

    @XmlAttribute(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "interface")
    public String getInterface() {
        return _interface;
    }

    public void setInterface(String _interface) {
        this._interface = _interface;
    }

    @XmlAttribute(name = "method")
    public String getMethod() {
        return _method;
    }

    public void setMethod(String _method) {
        this._method = _method;
    }

    @XmlAttribute(name = "scope")
    public String getScope() {
        return _scope;
    }

    public void setScope(String _scope) {
        this._scope = _scope;
    }

    public ServiceParams getParams() {
        return params;
    }

    public void setParams(ServiceParams params) throws ClassNotFoundException {
        this.params = params;
        paramTypes = new Class<?>[params.getParam().size()];
        Integer idx = 0;
        for (ServiceParam sp : params.getParam()) {
            paramTypes[idx] = Class.forName(sp.getClazz());
            idx++;
        }
    }

    public Class<?>[] getParameterTypes() {
        return paramTypes;
    }

    @Override
    public String toString() {
        return "Service{" + "id=" + id + ", name=" + name + ", interface=" + _interface + ", method=" + _method + '}';
    }
}
