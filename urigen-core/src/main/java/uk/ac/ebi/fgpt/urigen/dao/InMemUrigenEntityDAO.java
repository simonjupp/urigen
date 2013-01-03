package uk.ac.ebi.fgpt.urigen.dao;

import uk.ac.ebi.fgpt.urigen.exception.AutoIDException;
import uk.ac.ebi.fgpt.urigen.exception.UrigenException;
import uk.ac.ebi.fgpt.urigen.model.UrigenEntity;

import java.util.*;

/**
 * @author Simon Jupp
 * @date 03/01/2012
 * Functional Genomics Group EMBL-EBI
 */
public class InMemUrigenEntityDAO implements UrigenEntityDAO{

    private LinkedHashMap<Integer, UrigenEntity> entities;
    private int id = 0;

    public InMemUrigenEntityDAO () {
        entities = new LinkedHashMap<Integer, UrigenEntity>();
    }

    public UrigenEntity getEntityById(int id) {
        return entities.get(id);
    }

    public Collection<UrigenEntity> getAllEntities() {
        ArrayList v = new ArrayList<UrigenEntity>(entities.values());
        Collections.reverse(v);
        return v;
    }

    public Collection<UrigenEntity> getAllEntitiesByUser(int id) {
        Set<UrigenEntity> eps = new HashSet<UrigenEntity>();
        for (UrigenEntity e : entities.values()) {

            if (e.getUserId() == id) {
                eps.add(e);
            }
        }
        return eps;
    }

    public Collection<UrigenEntity> getAllEntitiesByPreferenceId(int id) {

        Set<UrigenEntity> eps = new HashSet<UrigenEntity>();
        for (UrigenEntity e : entities.values()) {

            if (e.getPreferencesId() == id) {
                eps.add(e);
            }
        }
        return eps;
    }

    public void RemoveEntity(UrigenEntity entity) {

    }

    public UrigenEntity saveUrigenEntity(UrigenEntity entity) throws AutoIDException {

        if (getEntityByUri(entity.getGeneratedUri().toString()) == null) {
            entity.setId(id);
            entities.put(id, entity);
            id++;
            return entity;
        }
        throw new AutoIDException("URI already exists in Urigen database, please try again: " + entity.getGeneratedUri().toString());
    }

    public UrigenEntity getEntityByUri(String uri) {
        for (UrigenEntity e : entities.values()) {
            if (e.getGeneratedUri().toString().equals(uri)) {
                return e;
            }
        }
        return  null;
    }
}
