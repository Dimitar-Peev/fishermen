package app.exception;

public class IncorrectUsernameOrPassword extends RuntimeException {
    public IncorrectUsernameOrPassword(String message) {
        super(message);
    }
}
