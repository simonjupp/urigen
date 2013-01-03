package uk.ac.ebi.fgpt.urigen.exception;

/**
 * @author Simon Jupp
 * @date 04/01/2012
 * Functional Genomics Group EMBL-EBI
 */
public class UserCreateException extends UrigenException {

    public UserCreateException() {
    }

    public UserCreateException(String message) {
        super(message);
    }

    public UserCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserCreateException(Throwable cause) {
        super(cause);
    }


}
