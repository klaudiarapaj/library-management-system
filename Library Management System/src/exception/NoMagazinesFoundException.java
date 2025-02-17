package exception;

//custom exception class to be used when a model.Magazine is not found, extends the Exception class
public class NoMagazinesFoundException extends  Exception {
    public NoMagazinesFoundException(String message) {
        super(message);
    }
}
