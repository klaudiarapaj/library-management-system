package exception;

//custom exception class to be used when a model.Book is not found, extends the Exception class
public class NoBooksFoundException extends Exception{
    public NoBooksFoundException(String message) {
        super(message);
    }
}
