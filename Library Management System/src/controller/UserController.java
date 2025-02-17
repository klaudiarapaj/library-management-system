package controller;

import exception.ItemNotAvailableException;
import exception.MaxBorrowLimitException;
import model.User;
import model.LibraryItem;
import model.LibraryManager;

import java.util.ArrayList;
import java.util.List;

public class UserController {

    private User model;
    private LibraryManager libraryManager;
    private List<LibraryItem> items;

    // constructor
    public UserController(User model, LibraryManager libraryManager, List<LibraryItem> items) {
        this.model = model;
        this.libraryManager = libraryManager;
        this.items = items;
    }

    // handle borrowing an item
    public String borrowItem(String itemTitle) {
        LibraryItem itemToBorrow = findItemByTitle(itemTitle);

        if (itemToBorrow != null) {
            try {
                libraryManager.borrowItem(model, itemToBorrow.getTitle());
                return "success";
            } catch (ItemNotAvailableException e) {
                return e.getMessage();
            } catch (MaxBorrowLimitException e) {
                throw new RuntimeException(e);
            }
        } else {
            return "Item not found.";
        }
    }

    // handle returning an item
    public String returnItem(String itemTitle) {
        LibraryItem itemToReturn = findItemByTitle(itemTitle);

        if (itemToReturn != null) {
            try {
                libraryManager.returnItem(model, itemToReturn.getTitle());
                return "success";
            } catch (ItemNotAvailableException e) {
                return e.getMessage();
            }
        } else {
            return "Item not found.";
        }
    }

    // handle searching an item by title
    public List<LibraryItem> searchItemByTitle(String title) throws ItemNotAvailableException{
        List<LibraryItem> results = new ArrayList<>();
        for (LibraryItem item : items) {
            if (item.getTitle().toLowerCase().contains(title.toLowerCase())) {
                results.add(item);
            }
        }

        if (results.isEmpty()) {
            throw new ItemNotAvailableException("Couldn't find any items matching the title: '" + title + "'.");
        }
        return results;
    }

    // get a list of borrowed items by the current user
    public List<LibraryItem> getBorrowedItems() {
        return model.getBorrowedItems();
    }

    // find an item by the title
    private LibraryItem findItemByTitle(String title) {
        for (LibraryItem item : items) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                return item;
            }
        }
        return null;
    }

    // get all library items
    public List<LibraryItem> getAllItems() {
        return model.getAllItems(items);
    }

    // logout method
    public boolean logout() {
        return true;
    }
}
