package model;

import database.AdminDAO;
import view.Observer;
import java.util.ArrayList;
import java.util.List;

// admin class is a subclass of the user class, here are defined the basic constructing knowledge, and methods for operations the admin can handle
public class Admin extends User implements Subject {
    private List<Observer> observers = new ArrayList<>(); // list of observers

    public Admin(String name, String email) {
        super(name, email);
    }

    // change the price of an Item (book/magazine)
    public void changeItemPrice(List<LibraryItem> items, int id, double newPrice) {
        for (LibraryItem item : items) { // find the item with the id inputted, id is unique
            if (item.getId() == id) {
                item.setPrice(newPrice);
                // persist the change to the database
                AdminDAO.changeItemPrice(id, newPrice);
                notifyObservers("💰 Price changed for: " + item.getTitle() + " → $" + newPrice);
                return;
            }
        }
    }

    // get all items in the library
    public List<LibraryItem> getAllItems(List<LibraryItem> items) {
        return items; // returns all library items for display
    }

    // get all registered users
    public List<User> getAllUsers(List<User> users) {
        return users; // returns the list of users
    }

    public boolean deleteUser(List<User> users, String email) {
        User userToDelete = null;

        // find the user with the matching email
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                userToDelete = user;
                break;
            }
        }

        if (userToDelete != null) {
            // remove the user from the in-memory list
            users.remove(userToDelete);

            // persist the change to the database
            AdminDAO.deleteUser(email);
            notifyObservers("❌ User deleted: " + userToDelete.getName() + " (" + email + ")"); //notification
            return true; // successfully deleted
        }

        return false; // user not found
    }


    // controlling observes to update when the admin class handles logic
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
