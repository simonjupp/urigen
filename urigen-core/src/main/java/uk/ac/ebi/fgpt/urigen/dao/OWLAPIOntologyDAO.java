package uk.ac.ebi.fgpt.urigen.dao;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * @author Simon Jupp
 * @date 03/01/2012
 * Functional Genomics Group EMBL-EBI
 */
public class OWLAPIOntologyDAO implements OWLOntologyDAO {

    private OWLOntologyManager manager;

    public OWLAPIOntologyDAO() {
        manager = OWLManager.createOWLOntologyManager();
    }

    public OWLOntologyManager getOWLOntologyManager() {
        return manager;
    }

    public void setOWLOntologyManager(OWLOntologyManager man) {
        this.manager = man;
    }

    public boolean ontologyContainsUri(String ontologyUri, String generatedUri) {
        return manager.contains(IRI.create(ontologyUri))
                && manager.getOntology(IRI.create(ontologyUri))
                .containsEntityInSignature(IRI.create(generatedUri));
    }
}
