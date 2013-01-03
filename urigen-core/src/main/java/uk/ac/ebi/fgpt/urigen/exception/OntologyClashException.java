package uk.ac.ebi.fgpt.urigen.exception;

/**
 * @author Simon Jupp
 * @date 23/02/2012
 * Functional Genomics Group EMBL-EBI
 */
public class OntologyClashException extends UrigenException {

    public OntologyClashException() {
    }

    public OntologyClashException(String message) {
        super(message);
    }

    public OntologyClashException(String message, Throwable cause) {
        super(message, cause);
    }

    public OntologyClashException(Throwable cause) {
        super(cause);
    }
}
