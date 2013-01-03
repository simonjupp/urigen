package uk.ac.ebi.fgpt.urigen.exception;

/**
 * @author Simon Jupp
 * @date 16/12/2011
 * Functional Genomics Group EMBL-EBI
 */
public class UrigenException extends Exception {

    public UrigenException() {
    }

    public UrigenException(String message) {
        super(message);
    }

    public UrigenException(String message, Throwable cause) {
        super(message, cause);
    }

    public UrigenException(Throwable cause) {
        super(cause);
    }

}
