package com.fedevela.engine.services.impls;

/**
 * Created by fvelazquez on 11/04/14.
 */
import com.fedevela.core.asic.beans.ExpurgoBean;
import com.fedevela.core.asic.beans.ExpurgosBean;
import com.fedevela.core.asic.definition.pojos.AdeamxDefinition;
import com.fedevela.core.asic.definition.pojos.AdeamxType;
import com.fedevela.core.asic.definition.pojos.VwDefinitionSearch;
import com.fedevela.core.asic.pojos.ChecklistCap;
import com.fedevela.asic.daos.DmsDao;
import com.fedevela.asic.util.TypeCast;
import com.fedevela.engine.EngineException;
import com.fedevela.engine.services.EngineService;
import com.fedevela.engine.swing.CheckBoxUI;
import com.fedevela.engine.swing.ComboBoxUI;
import com.fedevela.engine.swing.FieldUI;
import com.fedevela.engine.swing.NumberFieldUI;
import com.fedevela.engine.swing.TextFieldUI;
import com.fedevela.engine.swing.WindowUI;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import net.codicentro.core.Utils;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EngineServiceImpl implements EngineService {

    private final Logger logger = LoggerFactory.getLogger(EngineServiceImpl.class);
    @Resource
    private DmsDao dao;

    @Override
    public AdeamxDefinition getDefinition(final Long id) throws EngineException {
        return getDefinition(id, false);
    }

    @Override
    public AdeamxDefinition getDefinition(final Long id, boolean eagerMode) throws EngineException {
        AdeamxDefinition definition = dao.get(AdeamxDefinition.class, id);
        if (eagerMode) {
            definition.setAdeamxDefinitionList(getDefinitions(definition.getId(), eagerMode));
        }
        return definition;
    }

    @Override
    public AdeamxDefinition saveDefinition(AdeamxDefinition adeamxDefinition) throws EngineException {
        return dao.persist(adeamxDefinition);
    }

    @Override
    public void deleteDefinition(Long id) throws EngineException {
        AdeamxDefinition def = dao.get(AdeamxDefinition.class, id);
        if (def != null) {
            dao.delete(def);
        } else {
            throw new EngineException("El objeto \"Definition\" con id=" + id + " no existe.");
        }
    }

    @Override
    public AdeamxDefinition getDefinition(final String idOperation) throws EngineException {
        return getDefinition(idOperation, false);
    }

    @Override
    public AdeamxDefinition getDefinition(String idOperation, boolean eagerMode) throws EngineException {
        List<AdeamxDefinition> operations = getDefinitions(idOperation, eagerMode);
        String err = null;
        if ((operations == null) || (operations.isEmpty())) {
            err = "ERROR: No existe una definición para la operatoria " + idOperation + ".";
            logger.error(err);
            throw new EngineException(err);
        }
        return operations.get(0);
    }

    @Override
    public List<AdeamxDefinition> getDefinitions(final String idOperation) throws EngineException {
        return getDefinitions(idOperation, false);
    }

    @Override
    public List<AdeamxDefinition> getDefinitions(String idOperation, boolean eagerMode) throws EngineException {
        DetachedCriteria criteria = DetachedCriteria.forClass(AdeamxDefinition.class);
        criteria.add(Restrictions.eq("adeamxType.id", "OPERATION"));
        if (idOperation != null) {
            criteria.add(Restrictions.eq("feature", idOperation));
        }
        criteria.add(Restrictions.eq("rowStatus", 'A'));
        criteria.addOrder(Order.asc("eOrder"));
        List<AdeamxDefinition> definitions = dao.find(criteria);
        if (eagerMode) {
            for (AdeamxDefinition definition : definitions) {
                definition.setAdeamxDefinitionList(getDefinitions(definition.getId(), eagerMode));
            }
        }
        return definitions;
    }

    @Override
    public List<AdeamxDefinition> getDefinitions(final String idOperation, final String idFileType) throws EngineException {
        return getDefinitions(idOperation, idFileType, false);
    }

    @Override
    public List<AdeamxDefinition> getDefinitions(String idOperation, String idFileType, boolean eagerMode) throws EngineException {
        List<AdeamxDefinition> operations = getDefinitions(idOperation);
        String err = null;
        if ((operations == null) || (operations.isEmpty())) {
            err = "ERROR: No existe una definición para la operatoria " + idOperation + ".";
            logger.error(err);
            throw new EngineException(err);
        }
        if (operations.size() > 1) {
            err = "ERROR: Existe mas de una definición para la operatoria " + idOperation + ".";
            logger.error(err);
            throw new EngineException(err);
        }
        DetachedCriteria criteria = DetachedCriteria.forClass(AdeamxDefinition.class);
        criteria.add(Restrictions.eq("adeamxDefinition", operations.get(0)));
        criteria.add(Restrictions.eq("adeamxType.id", "FILE-TYPE"));
        if (!TypeCast.isBlank(idFileType)) {
            criteria.add(Restrictions.eq("feature", idFileType));
        }
        criteria.add(Restrictions.eq("rowStatus", 'A'));
        criteria.addOrder(Order.asc("eOrder"));
        List<AdeamxDefinition> definitions = dao.find(criteria);
        if (eagerMode) {
            for (AdeamxDefinition definition : definitions) {
                definition.setAdeamxDefinitionList(getDefinitions(definition.getId(), eagerMode));
            }
        }
        return definitions;
    }

    @Override
    public List<AdeamxDefinition> getDefinitions(String idOperation, String idFileType, String idDocType) throws EngineException {
        return getDefinitions(idOperation, idFileType, idDocType, false);
    }

    @Override
    public List<AdeamxDefinition> getDefinitions(String idOperation, String idFileType, String idDocType, boolean eagerMode) throws EngineException {
        List<AdeamxDefinition> fileTypes = getDefinitions(idOperation, idFileType);
        String err = null;
        if ((fileTypes == null) || (fileTypes.isEmpty())) {
            err = "ERROR: No existe una definición para la operatoria " + idOperation + ".";
            logger.error(err);
            throw new EngineException(err);
        }
        if (fileTypes.size() > 1) {
            err = "ERROR: Existe mas de una definición para la operatoria " + idOperation + ".";
            logger.error(err);
            throw new EngineException(err);
        }
        DetachedCriteria criteria = DetachedCriteria.forClass(AdeamxDefinition.class);
        criteria.add(Restrictions.eq("adeamxDefinition", fileTypes.get(0)));
        criteria.add(Restrictions.eq("adeamxType.id", "DOCUMENT-TYPE"));
        if (!TypeCast.isBlank(idDocType)) {
            criteria.add(Restrictions.eq("feature", idFileType));
        }
        criteria.add(Restrictions.eq("rowStatus", 'A'));
        criteria.addOrder(Order.asc("eOrder"));
        List<AdeamxDefinition> definitions = dao.find(criteria);
        if (eagerMode) {
            for (AdeamxDefinition definition : definitions) {
                definition.setAdeamxDefinitionList(getDefinitions(definition.getId(), eagerMode));
            }
        }
        return definitions;
    }

    @Override
    public List<AdeamxDefinition> getDefinitions() throws EngineException {
        return getDefinitionsEx(null);
    }

    @Override
    public List<AdeamxDefinition> getDefinitionsEx(Long idParent) throws EngineException {
        DetachedCriteria criteria = DetachedCriteria.forClass(AdeamxDefinition.class);
        if (idParent == null) {
            criteria.add(Restrictions.or(Restrictions.isNull("adeamxDefinition.id"), Restrictions.eqProperty("id", "adeamxDefinition.id")));
        } else {
            criteria.add(Restrictions.eq("adeamxDefinition.id", idParent));
        }
        criteria.addOrder(Order.asc("eOrder"));
        List<AdeamxDefinition> definitions = dao.find(criteria);
        for (AdeamxDefinition adeamxDefinition : definitions) {
            List<AdeamxDefinition> children = getDefinitions(adeamxDefinition.getId());
            adeamxDefinition.setHasChild(((children != null) && (!children.isEmpty())));
        }
        return definitions;
    }

    @Override
    public List<VwDefinitionSearch> getDefinitionSearch() throws EngineException {
        return getDefinitionSearch(null);
    }

    @Override
    public List<VwDefinitionSearch> getDefinitionSearch(Long idParent) throws EngineException {
        DetachedCriteria criteria = DetachedCriteria.forClass(VwDefinitionSearch.class);
        if (idParent == null) {
            criteria.add(Restrictions.or(Restrictions.isNull("parentId"), Restrictions.eqProperty("id", "parentId")));
        } else {
            criteria.add(Restrictions.eq("parentId", idParent));
        }
        criteria.addOrder(Order.asc("eOrder"));
        return dao.find(criteria);
    }

    @Override
    public List<AdeamxDefinition> getDefinitions(final Long idParent) throws EngineException {
        return getDefinitions(idParent, false);
    }

    @Override
    public List<AdeamxDefinition> getDefinitions(Long idParent, boolean eagerMode) throws EngineException {
        DetachedCriteria criteria = DetachedCriteria.forClass(AdeamxDefinition.class);
        criteria.add(Restrictions.eq("adeamxDefinition.id", idParent));
        criteria.add(Restrictions.eq("rowStatus", 'A'));
        criteria.addOrder(Order.asc("eOrder"));
        List<AdeamxDefinition> definitions = dao.find(criteria);
        if (eagerMode) {
            for (AdeamxDefinition definition : definitions) {
                definition.setAdeamxDefinitionList(getDefinitions(definition.getId(), eagerMode));
            }
        }
        return definitions;
    }

    @Override
    public List<AdeamxType> searchType(final String id) throws EngineException {
        DetachedCriteria criteria = DetachedCriteria.forClass(AdeamxType.class);
        if (!TypeCast.isBlank(id)) {
            criteria.add(Restrictions.like("id", id, MatchMode.ANYWHERE).ignoreCase());
        }
        return dao.find(criteria);
    }

    @Override
    public AdeamxType saveType(AdeamxType type) throws EngineException {
        return dao.persist(type);
    }

    @Override
    public void editExpurgo(final com.fedevela.engine.swing.WindowUI wnd, final ChecklistCap row) throws EngineException {
        editExpurgo(wnd, row, new Class[]{ExpurgosBean.class});
    }

    @Override
    public void editExpurgo(final WindowUI wnd, final ChecklistCap row, final Class[] _class) throws EngineException {
        if (_class == null) {
            throw new EngineException("El parámetro _class del método editExpurgo es obligatorio.");
        }
        if (!ArrayUtils.contains(_class, ExpurgosBean.class)) {
            throw new EngineException("El parámetro _class del método editExpurgo debe contener ExpurgosBean.class.");
        }
        // Limpiamos el formulario.
        wnd.getEngine().reset();
        if (row != null && !TypeCast.isBlank(row.getDato())) {
            ExpurgosBean exps = Utils.convertToEntity(row.getDato(), _class);
            for (ExpurgoBean exp : exps.getExpurgos()) {
                wnd.getEngine().setValue(exp.getPrefijo() + wnd.getDoccod() + "_" + exp.getIndice(), exp.getValor());
            }
        }
        wnd.setVisible(true);
        if (wnd.getWindowResult() == WindowUI.WindowResult.OK) {
            // Actualizamos los datos del expurgo.
            ExpurgosBean exps = new ExpurgosBean();
            exps.setExpurgos(new ArrayList<ExpurgoBean>());
            for (String key : wnd.getEngine().getInstances().keySet()) {
                if (key.startsWith("chkExp")) {
                    CheckBoxUI chk = (CheckBoxUI) wnd.getEngine().getComponent(key);
                    ExpurgoBean exp = new ExpurgoBean();
                    exp.setIndice(TypeCast.toShort(key.substring(key.lastIndexOf("_") + 1)));
                    exp.setPrefijo("chkExp");
                    exp.setDescripcion(chk.getText());// Actualmente lo estoy setiando por si se llega a ocupar en un reporte.
                    exp.setValor(chk.isSelected());
                    exps.getExpurgos().add(exp);
                } else if (key.startsWith("dteExp")) {
                    FieldUI fld = (FieldUI) wnd.getEngine().getComponent(key);
                    ExpurgoBean exp = new ExpurgoBean();
                    exp.setIndice(TypeCast.toShort(key.substring(key.lastIndexOf("_") + 1)));
                    exp.setPrefijo("dteExp");
                    exp.setDescripcion(fld.getLabel().getText());// Actualmente lo estoy setiando por si se llega a ocupar en un reporte.
                    exp.setValor(fld.getText());
                    exps.getExpurgos().add(exp);
                } else if (key.startsWith("cmbExp")) {
                    ComboBoxUI cmb = (ComboBoxUI) wnd.getEngine().getComponent(key);
                    ExpurgoBean exp = new ExpurgoBean();
                    exp.setIndice(TypeCast.toShort(key.substring(key.lastIndexOf("_") + 1)));
                    exp.setPrefijo("cmbExp");
                    exp.setDescripcion(cmb.getLabel().getText());
                    exp.setValor(cmb.getSelected());
                    exps.getExpurgos().add(exp);
                } else if (key.startsWith("nmbExp")) {
                    NumberFieldUI nmb = (NumberFieldUI) wnd.getEngine().getComponent(key);
                    ExpurgoBean exp = new ExpurgoBean();
                    exp.setIndice(TypeCast.toShort(key.substring(key.lastIndexOf("_") + 1)));
                    exp.setPrefijo("nmbExp");
                    exp.setDescripcion(nmb.getLabel().getText());
                    exp.setValor(nmb.getValue());
                    exps.getExpurgos().add(exp);
                } else if (key.startsWith("txtExp")) {
                    TextFieldUI nmb = (TextFieldUI) wnd.getEngine().getComponent(key);
                    ExpurgoBean exp = new ExpurgoBean();
                    exp.setIndice(TypeCast.toShort(key.substring(key.lastIndexOf("_") + 1)));
                    exp.setPrefijo("txtExp");
                    exp.setDescripcion(nmb.getLabel().getText());
                    exp.setValor(nmb.getText());
                    exps.getExpurgos().add(exp);
                }
            }
            row.setDato(Utils.convertToXml(exps, true, true, _class));
        }
    }

}
