package validation;

/**
 * Custom checked exception thrown when student data fails validation constraints.
 * 
 * Part of CIT300 DSA Project - Student Record Management Component
 * Assigned Member: MS Sarfaras (23DA2-0727)
 */
public class ValidationException extends Exception {

    /**
     * Constructs a ValidationException with a detailed message.
     *
     * @param message Description of validation error
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a ValidationException with a message and cause.
     *
     * @param message Description of validation error
     * @param cause   The underlying cause
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
