package uk.ac.ebi.fgpt.urigen.service;

import uk.ac.ebi.fgpt.urigen.exception.AutoIDException;
import uk.ac.ebi.fgpt.urigen.exception.OntologyClashException;
import uk.ac.ebi.fgpt.urigen.model.UrigenEntity;
import uk.ac.ebi.fgpt.urigen.model.UrigenRequest;

import java.util.Collection;

/**
 * @author Simon Jupp
 * @date 03/01/2012
 * Functional Genomics Group EMBL-EBI
 */
public interface UriCreationService {


    UrigenEntity generateNewUri(UrigenRequest request) throws AutoIDException, OntologyClashException;

    Collection<UrigenEntity> getGeneratedUris ();

    UrigenEntity updateUrigenEntity(UrigenEntity entity);

    void removeUrigenEntity(UrigenEntity entity);


}
