package my.solution.api.exceptions;

public class ValueAlreadyExistsException extends RuntimeException {
    public ValueAlreadyExistsException(String value) {
        super("Value of %s already belong to another client!".formatted(value));
    }

    public ValueAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public ValueAlreadyExistsException(Exception e) {
        super(e);
    }
}
