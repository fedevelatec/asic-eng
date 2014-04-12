package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Listener implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String type;
    private String _keyMap;
    private List<Service> service;
    private String script;

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(required = true)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Service> getService() {
        return service;
    }

    public void setService(List<Service> service) {
        this.service = service;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @XmlAttribute(name = "keyMap")
    public String getKeyMap() {
        return _keyMap;
    }

    public void setKeyMap(String _keyMap) {
        this._keyMap = _keyMap;
    }
}
