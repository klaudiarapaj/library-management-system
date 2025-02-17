package model;
import database.UserDAO;

import java.util.ArrayList;
import java.util.List;

// class for the user's profile, their details and functionalities they are allowed to execute
public class User{
    private static int idCounter = 0;
    private final int id;
    private final String name;
    private final String email;
    private final List<LibraryItem> borrowedItems;

    public User(String name, String email){
        this.id = ++idCounter;
        this.name = name;
        this.email = email;
        this.borrowedItems = new ArrayList<>();
    }

    // borrowedItems each user should have its own
    public List<LibraryItem> getBorrowedItems() {
        return borrowedItems;
    }

    // borrowItem method for each user to track their own
    public void borrowItem(LibraryItem item) {
        if (!item.isBorrowed()) {
            // update the item's status and notify observers
            item.borrowItem();

            // persist the change to the database
            UserDAO.borrowItem(this, item);

            // add to the user's borrowed list
            borrowedItems.add(item);
        } else {
            System.out.println("Item " + item.getTitle() + " is already borrowed.");
        }
    }

    // returnItem method to remove the item from the borrowed list
    public void returnItem(LibraryItem item){
        if (borrowedItems.contains(item)) {
            // update the item's status and notify observers
            item.returnItem();

            // persist the change to the database
            UserDAO.returnItem(item);

            // remove from the user's borrowed list
            borrowedItems.remove(item);
        }
    }

    public List<LibraryItem> getAllItems(List<LibraryItem> items) {
        return items; // returns all library items for display
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }
}
