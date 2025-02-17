package model.handlers;

import exception.ItemNotAvailableException;
import model.Magazine;
import model.User;

public class MagazineBorrowingHandler {
    // handlers for borrowing /returning a magazine
    public void handleBorrowing(User user, Magazine magazine) throws ItemNotAvailableException {
        if (magazine.isBorrowed()) {
            throw new ItemNotAvailableException("The magazine is already borrowed.");
        }
        user.borrowItem(magazine);
    }

    public void handleReturning(User user, Magazine magazine) {
        user.returnItem(magazine);
    }
}
