package exception;

//custom exception class to be used when an Item is not available, extends the Exception class
public class ItemNotAvailableException extends Exception {
    public ItemNotAvailableException(String message) {
        super(message);
    }
}