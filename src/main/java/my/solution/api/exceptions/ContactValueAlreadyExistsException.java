package my.solution.api.exceptions;

public class ContactValueAlreadyExistsException extends RuntimeException {
    public ContactValueAlreadyExistsException(String contactValue) {
        super("Contact value of %s already belong to another client!".formatted(contactValue));
    }
}
