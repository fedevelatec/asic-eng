package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "params")
public class ServiceParam implements Serializable {

    private static final long serialVersionUID = 1L;
    private String clazz;
    private String value;
    private String _scope;

    @XmlAttribute(name = "class")
    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @XmlValue
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @XmlAttribute(name = "scope")
    public String getScope() {
        return _scope;
    }

    public void setScope(String _scope) {
        this._scope = _scope;
    }
}
