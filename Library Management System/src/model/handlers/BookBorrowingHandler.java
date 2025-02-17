package model.handlers;

import exception.ItemNotAvailableException;
import model.Book;
import model.User;

public class BookBorrowingHandler {
    // handlers for borrowing /returning a book
    public void handleBorrowing(User user, Book book) throws ItemNotAvailableException {
        if (book.isBorrowed()) {
            throw new ItemNotAvailableException("The book is already borrowed.");
        }
        user.borrowItem(book);
    }

    public void handleReturning(User user, Book book) {
        user.returnItem(book);
    }
}
