package exception;

//custom exception class to be used when if a user has search the borrowing limit, extends the Exception class
public class MaxBorrowLimitException extends Exception {
    public MaxBorrowLimitException(String message) {
        super(message);
    }
}
