package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Listeners implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Listener> listener;

    @XmlElement(name = "listener")
    public List<Listener> getListeners() {
        return listener;
    }

    public void setListeners(List<Listener> listeners) {
        listener = listeners;
    }
}
