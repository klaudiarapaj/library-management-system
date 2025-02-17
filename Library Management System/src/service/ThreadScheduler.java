package service;

import model.LibraryItem;
import model.User;
import database.DatabaseConnection;
import java.util.List;
// refreshes the data periodically keeping the program up to data with changes made to the database by other threads
// saves the changes made during run-time in the database to ensure synchronization
public class ThreadScheduler {
    public static void main(String[] args) {
        startAutoRefreshTasks();
        startPeriodicFinalSave();
    }

    public static void startAutoRefreshTasks() {
        // refresh library items every minute
        Thread refreshLibraryItemsThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60000); // wait 1 minute
                    refreshLibraryItems();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        refreshLibraryItemsThread.setDaemon(true);
        refreshLibraryItemsThread.start();

        // refresh users every minute
        Thread refreshUsersThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60000);
                    refreshUsers();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        refreshUsersThread.setDaemon(true);
        refreshUsersThread.start();
    }

    // ensures the last changes are saved to the database
    public static void startPeriodicFinalSave() {
        Thread saveThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(300000); // save every 5 minutes
                    saveDataToDatabase();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        saveThread.setDaemon(true);
        saveThread.start();
    }

    // refresh library items from the database
    public static void refreshLibraryItems() {
        List<User> users = DatabaseConnection.loadUsers();
        List<LibraryItem> items = DatabaseConnection.loadLibraryItems(users);
        updateInventoryList(items);
        System.out.println("Library inventory has been refreshed.");
    }

    // refresh users from the database
    public static void refreshUsers() {
        List<User> users = DatabaseConnection.loadUsers();
        updateUserList(users);
        System.out.println("Users list has been refreshed.");
    }

    // save data to the database after refreshing
    public static void saveDataToDatabase() {
        List<User> users = DatabaseConnection.loadUsers();
        List<LibraryItem> items = DatabaseConnection.loadLibraryItems(users);

        DatabaseConnection.saveData(users, items);
        System.out.println("Final application state saved to database.");
    }

    // Print message when library inventory is refreshed
    public static void updateInventoryList(List<LibraryItem> updatedItems) {
        System.out.println("Library inventory updated with " + updatedItems.size() + " items.");
    }

    // Print message when users list is refreshed
    public static void updateUserList(List<User> updatedUsers) {
        System.out.println("Users list updated with " + updatedUsers.size() + " users.");
    }
}
