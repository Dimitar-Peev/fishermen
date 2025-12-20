package app.exception;

public class FishermanNotFoundException extends RuntimeException {
    public FishermanNotFoundException(String message) {
        super(message);
    }
}
