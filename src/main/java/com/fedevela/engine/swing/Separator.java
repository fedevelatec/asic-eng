package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Separator implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer _size;
    private String _orientation;

    public Separator() {
    }

    @XmlAttribute(name = "size")
    public Integer getSize() {
        return _size;
    }

    public void setSize(Integer _size) {
        this._size = _size;
    }

    @XmlAttribute(name = "orientation")
    public String getOrientation() {
        return _orientation;
    }

    public void setOrientation(String _orientation) {
        this._orientation = _orientation;
    }

}
