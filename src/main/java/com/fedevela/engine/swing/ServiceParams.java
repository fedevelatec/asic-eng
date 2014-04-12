package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "params")
public class ServiceParams implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<ServiceParam> param;

    @XmlElement(name = "value")
    public List<ServiceParam> getParam() {
        return param;
    }

    public void setParam(List<ServiceParam> param) {
        this.param = param;
    }
}
