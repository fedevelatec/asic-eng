package com.fedevela.engine.swing.grid;

/**
 * Created by fvelazquez on 11/04/14.
 */
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Column implements Serializable {

    private static final long serialVersionUID = 1L;
    @XmlAttribute(required = true)
    private String name;
    @XmlValue
    private String header;
    @XmlAttribute
    private String width;
    @XmlAttribute
    private Boolean resizable = true;
    @XmlAttribute
    private Boolean editable = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public Boolean isResizable() {
        return resizable;
    }

    public void setResizable(Boolean resizable) {
        this.resizable = resizable;
    }

    public Boolean isEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    @Override
    public String toString() {
        return header;
    }
}
