// Worked by Klaudia Rapaj
package main;

import controller.AdminController;
import controller.UserController;
import model.*;
import database.DatabaseConnection;
import service.ThreadScheduler;
import view.AdminView;
import view.SystemView;
import view.UserView;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<User> users_mutable;  // Mutable user list
    private static List<User> users;          // Immutable user list
    private static List<LibraryItem> items_mutable;  // Mutable item list
    private static List<LibraryItem> items;          // Immutable item list

    private static LibraryManager libraryManager_mutable;
    private static UserView userView;
    private static AdminView adminView;
    private static SystemView systemView;

    //load tables from database and get the initial state of the program
    static {
        DatabaseConnection.createTables();
        users = DatabaseConnection.loadUsers();
        items = DatabaseConnection.loadLibraryItems(users);

        users_mutable = new ArrayList<>(users);
        items_mutable = new ArrayList<>(items);

        libraryManager_mutable = new LibraryManager(items_mutable);

        systemView = new SystemView();
        System.out.println("Initializing the system...");
        ThreadScheduler.startAutoRefreshTasks();

    }

    public static void main(String[] args) {
        //display the main menu of the program, call the respective methods to access the program
        systemView.displayMainMenu(new SystemView.MainMenuListener() {
            @Override
            public void onRegisterSelected() {
                registerUser();
            }

            @Override
            public void onLoginSelected() {
                loginExistingUser();
            }

            @Override
            public void onExitSelected() {
                systemView.showMessage("Exiting... Bye!");
                DatabaseConnection.saveData(users_mutable, items_mutable); //save data on exit
                System.exit(0);
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Closing the system...");
            DatabaseConnection.saveData(users_mutable, items_mutable); //ensure data is saved
        }));
    }

    //register the user and save into the database
    private static void registerUser() {

        User newUser = systemView.showRegisterForm();
        if (newUser != null) {
            if (isEmailRegistered(newUser.getEmail())) {
                systemView.showMessage("This email is already registered. Please use a different email.");
                return;
            }
            users_mutable.add(newUser);
            users = new ArrayList<>(users_mutable);
            systemView.showMessage("Registration successful!");

            // Save updated users list
            DatabaseConnection.saveData(users_mutable, items_mutable);
        }
    }

    //register the existing user, perform the validation
    private static void loginExistingUser() {
        String email = systemView.showLoginForm();
        if (email == null || email.isBlank()) {
            systemView.showMessage("Invalid email.");
            return;
        }

        User user = findUserByEmail(email);
        if (user != null) {
            systemView.showLoginSuccess(user);
            handleLoggedInUser(user);
        } else {
            systemView.showLoginFailure();
        }
    }

    private static User findUserByEmail(String email) {
        return users.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst().orElse(null);
    }

    private static boolean isEmailRegistered(String email) {
        return users.stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    //handled the logged in user to display the dedicated home menu
    private static void handleLoggedInUser(User user) {
        if (user instanceof Admin) {
            AdminController adminController = new AdminController(
                    (Admin) user, items_mutable, libraryManager_mutable, users_mutable);
            adminView = new AdminView(adminController, (Admin) user);
        } else {
            UserController userController = new UserController(
                    user, libraryManager_mutable, items_mutable);
            userView = new UserView(userController);
        }
    }
}
