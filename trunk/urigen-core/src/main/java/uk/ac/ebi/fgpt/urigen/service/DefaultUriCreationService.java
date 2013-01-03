package uk.ac.ebi.fgpt.urigen.service;

import uk.ac.ebi.fgpt.urigen.exception.AutoIDException;
import uk.ac.ebi.fgpt.urigen.exception.OntologyClashException;
import uk.ac.ebi.fgpt.urigen.model.UrigenEntity;
import uk.ac.ebi.fgpt.urigen.model.UrigenManager;
import uk.ac.ebi.fgpt.urigen.model.UrigenRequest;

import java.util.Collection;

/**
 * @author Simon Jupp
 * @date 29/02/2012
 * Functional Genomics Group EMBL-EBI
 */
public class DefaultUriCreationService implements UriCreationService{

    private UrigenManager manager;

    public void setUrigenManager (UrigenManager manager) {
        this.manager = manager;
    }

    public UrigenEntity generateNewUri(UrigenRequest request) throws AutoIDException, OntologyClashException {
        return manager.getNewEntity(request);
    }

    public Collection<UrigenEntity> getGeneratedUris() {
        return manager.getAllGeneratedUris();
    }

    public UrigenEntity updateUrigenEntity(UrigenEntity entity) {
        return null;
    }

    public void removeUrigenEntity(UrigenEntity entity) {

    }
}
