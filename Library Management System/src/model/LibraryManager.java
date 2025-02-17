package model;

import exception.ItemNotAvailableException;
import exception.MaxBorrowLimitException;
import model.handlers.BookBorrowingHandler;
import model.handlers.MagazineBorrowingHandler;

import java.util.List;

// libraryManager delegates borrowing/returning tasks to respective handlers
public class LibraryManager implements LibraryActions {

    private List<LibraryItem> items;
    private final BookBorrowingHandler bookHandler;  // book handler
    private final MagazineBorrowingHandler magazineHandler;  // magazine handler

    // constructor
    public LibraryManager(List<LibraryItem> items) {
        this.items = items;
        this.bookHandler = new BookBorrowingHandler();
        this.magazineHandler = new MagazineBorrowingHandler();
    }

    // borrowing logic is delegated
    @Override
    public void borrowItem(User user, String title) throws ItemNotAvailableException, MaxBorrowLimitException {
        LibraryItem itemToBorrow = findItemByTitle(title);

        if (itemToBorrow == null) {
            throw new ItemNotAvailableException("Item does not exist.");
        }

        int totalBorrowedItems = user.getBorrowedItems().size();

        if (totalBorrowedItems >= 5) {
            throw new MaxBorrowLimitException("You cannot borrow more than 5 items.");
        }

        if (itemToBorrow instanceof Book) {
            bookHandler.handleBorrowing(user, (Book) itemToBorrow);
        } else if (itemToBorrow instanceof Magazine) {
            magazineHandler.handleBorrowing(user, (Magazine) itemToBorrow);
        } else {
            throw new ItemNotAvailableException("Unsupported item type.");
        }
    }

    // returning logic is delegated
    @Override
    public void returnItem(User user, String title) throws ItemNotAvailableException {
        LibraryItem itemToReturn = user.getBorrowedItems()
                .stream()
                .filter(item -> item.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);

        if (itemToReturn == null) {
            throw new ItemNotAvailableException("Item not found in your borrowed items.");
        }

        if (itemToReturn instanceof Book) {
            bookHandler.handleReturning(user, (Book) itemToReturn);
        } else if (itemToReturn instanceof Magazine) {
            magazineHandler.handleReturning(user, (Magazine) itemToReturn);
        } else {
            throw new ItemNotAvailableException("Unsupported item type.");
        }
    }

    public void addItem(LibraryItem item) {
        items.add(item);
    }

    public List<LibraryItem> getItems() {
        return items;
    }

    private LibraryItem findItemByTitle(String title) {
        return items.stream()
                .filter(item -> item.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }
}
