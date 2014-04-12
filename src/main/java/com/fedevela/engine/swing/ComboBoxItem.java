package com.fedevela.engine.swing;

/**
 * Created by fvelazquez on 11/04/14.
 */
import com.fedevela.asic.util.TypeCast;
import com.fedevela.engine.EngineException;
import net.codicentro.cliser.dao.CliserDao;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class ComboBoxItem implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum TType {

        FIXED_ARRAY,
        SQL,
        REFERENCE_CMP
    }
    private TType type;
    private String value;
    private Class entityClass;
    private Object selected;
    @XmlAttribute(name = "id")
    private String _id;
    @XmlAttribute(name = "scope")
    private String _scope;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public TType getType() {
        return type;
    }

    public void setType(TType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    public Object getSelected() {
        return selected;
    }

    public void setSelected(Object selected) {
        this.selected = selected;
    }

    public Object[] getValues() throws EngineException {
        return getValues(null);
    }

    public String getScope() {
        return _scope;
    }

    public void setScope(String _scope) {
        this._scope = _scope;
    }

    public Object[] getValues(Object obj) throws EngineException {
        Object[] rs = null;
        try {
            switch (type) {
                case FIXED_ARRAY:
                    rs = TypeCast.isBlank(_id) ? TypeCast.toArray(value) : ((List) obj).toArray();
                    break;
                case SQL:
                    CliserDao dao = (CliserDao) obj;
                    rs = (entityClass == null) ? dao.find(new StringBuilder(value)).toArray(new Object[]{}) : dao.find(entityClass, new StringBuilder(value)).toArray(new Object[]{});
                    break;
                case REFERENCE_CMP:
                    ComboBoxUI reference = (ComboBoxUI) obj;
                    rs = new Object[reference.getModel().getSize()];
                    for (int i = 0; i < reference.getModel().getSize(); i++) {
                        rs[ i] = reference.getModel().getElementAt(i);
                    }
                    break;
            }
            return rs;
        } catch (Exception ex) {
            throw new EngineException("Ha ocurrido un error al obtener los elementos del ComboBox " + _id + ".", ex);
        }
    }
}
