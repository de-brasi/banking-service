package my.solution.api.exceptions;

public class LastContactRemoveException extends RuntimeException {
    public LastContactRemoveException(String removedFieldName) {
        super("Removing last contact: %s".formatted(removedFieldName));
    }
}
