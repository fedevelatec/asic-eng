package com.fedevela.engine.services;

/**
 * Created by fvelazquez on 11/04/14.
 */
import com.fedevela.core.asic.definition.pojos.AdeamxDefinition;
import com.fedevela.core.asic.definition.pojos.AdeamxType;
import com.fedevela.core.asic.definition.pojos.VwDefinitionSearch;
import com.fedevela.core.asic.pojos.ChecklistCap;
import com.fedevela.engine.EngineException;
import com.fedevela.engine.swing.WindowUI;
import java.util.List;

public interface EngineService {

    /**
     *
     * @param id
     * @return
     */
    public AdeamxDefinition getDefinition(final Long id) throws EngineException;

    public AdeamxDefinition getDefinition(final Long id, final boolean eagerMode) throws EngineException;

    public AdeamxDefinition saveDefinition(AdeamxDefinition adeamxDefinition) throws EngineException;

    public void deleteDefinition(final Long id) throws EngineException;

    /**
     *
     * @param idOperation
     * @return
     * @throws EngineException
     */
    public AdeamxDefinition getDefinition(final String idOperation) throws EngineException;

    public AdeamxDefinition getDefinition(final String idOperation, final boolean eagerMode) throws EngineException;

    /**
     * Metodo para obtener todos los elementos que son raiz. Criterio: Obener
     * todos los elementos que (PARENT_ID=NULL) ó (ID=PARENT_ID) Nota: Este
     * método es utilizado principalmente en el cpanel para la administración de
     * los elementos ya que no filtra los elementos que están
     * activos.(ROWSTATUS=A)
     *
     * @return
     * @throws EngineException
     */
    public List<AdeamxDefinition> getDefinitions() throws EngineException;

    public List<AdeamxDefinition> getDefinitionsEx(final Long idParent) throws EngineException;

    public List<VwDefinitionSearch> getDefinitionSearch() throws EngineException;

    public List<VwDefinitionSearch> getDefinitionSearch(final Long idParent) throws EngineException;

    /**
     *
     * @param idParent
     * @return
     * @throws EngineException
     */
    public List<AdeamxDefinition> getDefinitions(final Long idParent) throws EngineException;

    public List<AdeamxDefinition> getDefinitions(final Long idParent, final boolean eagerMode) throws EngineException;

    /**
     *
     * @param idOperation
     * @return
     * @throws EngineException
     */
    public List<AdeamxDefinition> getDefinitions(final String idOperation) throws EngineException;

    public List<AdeamxDefinition> getDefinitions(final String idOperation, final boolean eagerMode) throws EngineException;

    /**
     *
     * @param idOperation
     * @param idFileType
     * @return
     * @throws EngineException
     */
    public List<AdeamxDefinition> getDefinitions(final String idOperation, final String idFileType) throws EngineException;

    public List<AdeamxDefinition> getDefinitions(final String idOperation, final String idFileType, final boolean eagerMode) throws EngineException;

    /**
     *
     * @param idOperation
     * @param idFileType
     * @param idDocType
     * @return
     * @throws EngineException
     */
    public List<AdeamxDefinition> getDefinitions(final String idOperation, final String idFileType, final String idDocType) throws EngineException;

    public List<AdeamxDefinition> getDefinitions(final String idOperation, final String idFileType, final String idDocType, final boolean eagerMode) throws EngineException;

    public List<AdeamxType> searchType(final String id) throws EngineException;

    public AdeamxType saveType(AdeamxType type) throws EngineException;

    public void editExpurgo(final WindowUI wnd, final ChecklistCap row) throws EngineException;

    public void editExpurgo(final WindowUI wnd, final ChecklistCap row, final Class[] _class) throws EngineException;
}
