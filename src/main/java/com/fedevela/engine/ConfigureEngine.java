package com.fedevela.engine;

/**
 * Created by fvelazquez on 11/04/14.
 */
import com.fedevela.asic.util.TypeCast;
import com.fedevela.engine.swing.ComboBoxItem;
import com.fedevela.engine.swing.Label;
import com.fedevela.engine.swing.Listeners;
import com.fedevela.engine.swing.Service;
import com.fedevela.engine.swing.grid.ColumnModel;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "configure")
public class ConfigureEngine implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String text;
    private String title;
    private String type;
    private Boolean textWrap;
    private Integer _width;
    private Integer _height;
    private Integer _left;
    private Integer _top;
    private Integer _separator;
    /**
     * @deprecated Utilizar la propiedad de br
     */
    private Integer newLine;
    private Boolean br;//Line break
    private Boolean disabled;
    private ComboBoxItem item;
    private Listeners listeners;
    private ColumnModel columns;
    private Boolean copy;
    private Boolean paste;
    private Boolean cut;
    private Boolean modal;
    private Boolean allowBlank;
    private String pattern;
    private Integer maxLength;
    private Integer minLength;
    private Boolean doubleTyping;
    private Short decimalPlaces;
    private Label _label;
    private Boolean visible;
    private Boolean validateOnBlur;
    private List<Service> service;
    private String caseSensitive = "NONE";
    private Boolean doubleTypingEnter;
    private Integer fontSize;
    private String icon;
    private Long doccod;
    private String _instances;

    public ConfigureEngine() {
        disabled = false;
        textWrap = false;
        modal = true;
        allowBlank = true;
        doubleTyping = false;
        visible = true;
        validateOnBlur = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWidth() {
        return _width;
    }

    public void setWidth(Integer _width) {
        this._width = _width;
    }

    public Integer getHeight() {
        return _height;
    }

    public void setHeight(Integer _height) {
        this._height = _height;
    }

    public Integer getLeft() {
        return _left;
    }

    public void setLeft(Integer _left) {
        this._left = _left;
    }

    public Integer getTop() {
        return _top;
    }

    public void setTop(Integer _top) {
        this._top = _top;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ComboBoxItem getItem() {
        return item;
    }

    public void setItem(ComboBoxItem item) {
        this.item = item;
    }

    public String getInstances() {
        return _instances;
    }

    public void setInstances(String _instances) {
        this._instances = _instances;
    }

    @Deprecated
    public Integer getNewLine() {
        return newLine;
    }

    @Deprecated
    public void setNewLine(Integer newLine) {
        this.newLine = newLine;
    }

    public Boolean isBr() {
        return br;
    }

    public void setBr(Boolean br) {
        this.br = br;
    }

    public Boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Listeners getListeners() {
        return listeners;
    }

    public void setListeners(Listeners listeners) {
        this.listeners = listeners;
    }

    public Boolean isCopy() {
        return copy;
    }

    public void setCopy(Boolean copy) {
        this.copy = copy;
    }

    public Boolean isPaste() {
        return paste;
    }

    public void setPaste(Boolean paste) {
        this.paste = paste;
    }

    public Boolean isCut() {
        return cut;
    }

    public void setCut(Boolean cut) {
        this.cut = cut;
    }

    public Boolean isTextWrap() {
        return textWrap;
    }

    public void setTextWrap(Boolean textWrap) {
        this.textWrap = textWrap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isModal() {
        return modal;
    }

    public void setModal(Boolean modal) {
        this.modal = modal;
    }

    public Boolean isAllowBlank() {
        return allowBlank;
    }

    public void setAllowBlank(Boolean allowBlank) {
        this.allowBlank = allowBlank;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Boolean isDoubleTyping() {
        return doubleTyping;
    }

    public void setDoubleTyping(Boolean doubleTyping) {
        this.doubleTyping = doubleTyping;
    }

    public Boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Label getLabel() {
        return _label;
    }

    public void setLabel(Label _label) {
        this._label = _label;
    }

    public Boolean isValidateOnBlur() {
        return validateOnBlur;
    }

    public void setValidateOnBlur(Boolean validateOnBlur) {
        this.validateOnBlur = validateOnBlur;
    }

    public List<Service> getService() {
        return service;
    }

    public void setService(List<Service> service) {
        this.service = service;
    }

    public Short getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(Short decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public String getCaseSensitive() {
        return TypeCast.isBlank(caseSensitive) ? "NONE" : caseSensitive.toUpperCase();
    }

    public void setCaseSensitive(String caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConfigureEngine other = (ConfigureEngine) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    /**
     * @return the doubleTypingEnter
     */
    public Boolean getDoubleTypingEnter() {
        return doubleTypingEnter;
    }

    /**
     * @param doubleTypingEnter the doubleTypingEnter to set
     */
    public void setDoubleTypingEnter(Boolean doubleTypingEnter) {
        this.doubleTypingEnter = doubleTypingEnter;
    }

    /**
     * @return the fontSize
     */
    public Integer getFontSize() {
        return fontSize;
    }

    /**
     * @param fontSize the fontSize to set
     */
    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getDoccod() {
        return doccod;
    }

    public void setDoccod(Long doccod) {
        this.doccod = doccod;
    }

    public ColumnModel getColumns() {
        return columns;
    }

    public void setColumns(ColumnModel columns) {
        this.columns = columns;
    }
}
