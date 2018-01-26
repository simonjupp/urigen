package uk.ac.ebi.fgpt.urigen.dao;

import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * @author Simon Jupp
 * @date 18/12/2011
 * Functional Genomics Group EMBL-EBI
 */
public interface OWLOntologyDAO {

    OWLOntologyManager getOWLOntologyManager();

    boolean ontologyContainsUri(String ontologyUri, String generatedUri);
}
