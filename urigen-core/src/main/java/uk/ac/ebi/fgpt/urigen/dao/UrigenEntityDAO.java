package uk.ac.ebi.fgpt.urigen.dao;

import uk.ac.ebi.fgpt.urigen.exception.AutoIDException;
import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
import uk.ac.ebi.fgpt.urigen.model.UrigenEntity;
import uk.ac.ebi.fgpt.urigen.model.UrigenPreference;

import java.util.Collection;

/**
 * @author Simon Jupp
 * @date 15/12/2011
 * Functional Genomics Group EMBL-EBI
 */
public interface UrigenEntityDAO {

    UrigenEntity getEntityById (int id);

    Collection<UrigenEntity> getAllEntities ();

    Collection<UrigenEntity> getAllEntitiesByUser (int id);

    Collection<UrigenEntity> getAllEntitiesByPreferenceId (int id);

    void RemoveEntity (UrigenEntity entity);

    UrigenEntity saveUrigenEntity (UrigenEntity entity) throws AutoIDException;

    UrigenEntity getEntityByUri (String uri);



}
