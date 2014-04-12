package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
public class Label implements Serializable {

    private static final long serialVersionUID = 1L;
    private String _text;
    private Integer _width;
    private Integer _height;

    public Label() {
    }

    @XmlValue
    public String getText() {
        return _text;
    }

    public void setText(String _text) {
        this._text = _text;
    }

    @XmlAttribute(name = "width")
    public Integer getWidth() {
        return _width;
    }

    public void setWidth(Integer _width) {
        this._width = _width;
    }

    @XmlAttribute(name = "height")
    public Integer getHeight() {
        return _height;
    }

    public void setHeight(Integer _height) {
        this._height = _height;
    }
}
