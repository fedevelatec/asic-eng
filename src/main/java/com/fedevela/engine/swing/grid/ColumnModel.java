package com.fedevela.engine.swing.grid;

/**
 * Created by fvelazquez on 11/04/14.
 */
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "columns")
public class ColumnModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private List<Column> column;

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "column")
    public List<Column> getColumns() {
        return column;
    }

    public void setColumns(List<Column> column) {
        this.column = column;
    }
}
