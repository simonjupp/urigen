package uk.ac.ebi.fgpt.urigen.exception;

/**
 * @author Simon Jupp
 * @date 23/02/2012
 * Functional Genomics Group EMBL-EBI
 */
public class PreferenceCreationException extends UrigenException {

    public PreferenceCreationException() {
    }

    public PreferenceCreationException(String message) {
        super(message);
    }

    public PreferenceCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PreferenceCreationException(Throwable cause) {
        super(cause);
    }


}
