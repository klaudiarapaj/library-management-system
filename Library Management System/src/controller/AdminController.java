package controller;

import exception.ItemNotAvailableException;
import model.*;
import database.AdminDAO;


import java.util.ArrayList;
import java.util.List;

public class AdminController {

    private Admin model;
    private List<LibraryItem> items;
    private LibraryManager libraryManager;
    private List<User> users;

    //constructor
    public AdminController(Admin model, List<LibraryItem> items, LibraryManager libraryManager, List<User> users) {
        this.model = model;
        this.items = items;
        this.libraryManager = libraryManager;
        this.users = users;
    }

    public void addBook(String title, int year, double price, String author, String genre) {
        Book newBook = new Book(-1, title, year, price, author, genre);
        int generatedId = AdminDAO.insertBook(newBook);

        if (generatedId != -1) {
            // Update the Book's ID with the database-generated ID
            newBook.setId(generatedId);

            // Add to the in-memory list
            libraryManager.addItem(newBook);
        } else {
            System.out.println("Failed to add book to the database.");
        }
    }

    public void addMagazine(String title, int year, double price, int issueNumber) {
        Magazine newMagazine = new Magazine(-1, title, year, price, issueNumber);
        // Persist to the database and get the generated ID
        int generatedId = AdminDAO.insertMagazine(newMagazine);

        if (generatedId != -1) {
            // Update the Magazine's ID with the database-generated ID
            newMagazine.setId(generatedId);

            // Add to the in-memory list
            libraryManager.addItem(newMagazine);
        } else {
            System.out.println("Failed to add magazine to the database.");
        }
    }

    // search items by title
    public List<LibraryItem> searchItemByTitle(String title) throws ItemNotAvailableException {
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

    public User getUserByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null); // return null if no user is found
    }



    // delete user by email
    public boolean deleteUser(String email) {
        return model.deleteUser(users, email);
    }

    // change the price of an item
    public void changeItemPrice(int id, double newPrice) {
        model.changeItemPrice(items, id, newPrice);
    }

    // get all library items
    public List<LibraryItem> getAllItems() {
        return model.getAllItems(items);
    }

    // get all registered users
    public List<User> getAllUsers() {
        return model.getAllUsers(users);
    }

    // get borrowed items across all users
    public List<LibraryItem> getAllBorrowedItems() {
        List<LibraryItem> borrowedItems = new ArrayList<>();
        for (User user : users) {
            borrowedItems.addAll(user.getBorrowedItems());
        }
        return borrowedItems;
    }

    public LibraryItem findItemById(int id) {
        for (LibraryItem item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null; // return null if the item with the given ID is not found
    }


    // get borrowed items for a specific user by email
    public List<LibraryItem> getUserBorrowedItems(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user.getBorrowedItems();
            }
        }
        return new ArrayList<>(); // return empty list if user not found
    }

    public boolean logout() {
        return true;
    }


}
