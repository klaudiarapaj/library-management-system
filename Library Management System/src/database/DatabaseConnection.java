package database;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//connects to an external MySQL database
//handles the loading to the in-memory list and saving from the in-memory list into the database

public class DatabaseConnection {
    //database credentials to connect
    private static final String USER = "root";
    private static final String PASSWORD = "";

    //connect to the jdbc driver
    public static Connection getConnection() throws SQLException {
        String dbName = "library";
        String dbUrl = "jdbc:mysql://localhost:3306/";
        // incase the sql script isn't executed, the database and tables can be created but must be populated at runtime
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // connect to MySQL without specifying a database
            try (Connection conn = DriverManager.getConnection(dbUrl, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {

                // create the database if it doesn't exist
                String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + dbName;
                stmt.executeUpdate(createDatabaseQuery);
            }

            // now connect to the specified database
            return DriverManager.getConnection(dbUrl + dbName, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }


    // create the tables if they don't exist : Users, Library_items
    public static void createTables() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "email VARCHAR(255) UNIQUE, " +
                "role ENUM('admin', 'user') NOT NULL)";

        String createItemsTable = "CREATE TABLE IF NOT EXISTS library_items (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "title VARCHAR(255), " +
                "year INT, " +
                "price DOUBLE, " +
                "type ENUM('book', 'magazine'), " +
                "author VARCHAR(255), " +
                "genre VARCHAR(255), " +
                "issue_number INT, " +
                "borrowed_status BOOLEAN DEFAULT FALSE, " +
                "borrowed_by VARCHAR(255) DEFAULT NULL)";

        // execute the statements
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createItemsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // load the users from the database and save them to the Users list which is utilized during the execution
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String role = rs.getString("role");

                //ensure the admin is of the correct role type
                if ("admin".equalsIgnoreCase(role)) {
                    users.add(new Admin(name, email));
                } else {
                    users.add(new User(name, email));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // load the library items from the database and save them in the Items list which is utilized during the execution
    public static List<LibraryItem> loadLibraryItems(List<User> users) {
        List<LibraryItem> items = new ArrayList<>();
        String query = "SELECT * FROM library_items";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                int year = rs.getInt("year");
                double price = rs.getDouble("price");
                String type = rs.getString("type");
                boolean borrowedStatus = rs.getBoolean("borrowed_status");
                String borrowedByEmail = rs.getString("borrowed_by");

                LibraryItem item = null;
                //ensure the item is of the correct type
                if ("book".equalsIgnoreCase(type)) {
                    item = new Book(id,title, year, price, rs.getString("author"), rs.getString("genre"));
                } else if ("magazine".equalsIgnoreCase(type)) {
                    item = new Magazine(id,title, year, price, rs.getInt("issue_number"));
                }

                if (item != null) { //get the borrowed status which is a column only in the database
                    item.setBorrowed(borrowedStatus);
                    items.add(item);

                    // assign the borrowed item to the correct user by checking the borrowedbyEmail column that is available in the database
                    if (borrowedByEmail != null) {
                        for (User user : users) {
                            if (user.getEmail().equals(borrowedByEmail)) {
                                user.getBorrowedItems().add(item);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // save the data by upserting (updating/inserting) into the database for higher performance
    public static void saveData(List<User> users, List<LibraryItem> items) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // get all existing users from the database
            List<String> existingUserEmails = new ArrayList<>();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT email FROM users")) {
                while (rs.next()) {
                    existingUserEmails.add(rs.getString("email"));
                }
            }

            // insert or update users
            PreparedStatement userStmt = conn.prepareStatement(
                    "INSERT INTO users (name, email, role) VALUES (?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE name = VALUES(name), role = VALUES(role)"
            );


            // insert or update library items
            PreparedStatement itemStmt = conn.prepareStatement(
                    "INSERT INTO library_items (id, title, year, price, type, author, genre, issue_number, borrowed_status, borrowed_by) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE title = VALUES(title), year = VALUES(year), price = VALUES(price), " +
                            "type = VALUES(type), author = VALUES(author), genre = VALUES(genre), " +
                            "issue_number = VALUES(issue_number), borrowed_status = VALUES(borrowed_status), borrowed_by = VALUES(borrowed_by)"
            );

            for (LibraryItem item : items) {
                itemStmt.setInt(1, item.getId());
                itemStmt.setString(2, item.getTitle());
                itemStmt.setInt(3, item.getYear());
                itemStmt.setDouble(4, item.getPrice());

                if (item instanceof Book) {
                    Book book = (Book) item;
                    itemStmt.setString(5, "book");
                    itemStmt.setString(6, book.getAuthor());
                    itemStmt.setString(7, book.getGenre());
                    itemStmt.setNull(8, java.sql.Types.INTEGER); // no issue number for books
                } else if (item instanceof Magazine) {
                    Magazine magazine = (Magazine) item;
                    itemStmt.setString(5, "magazine");
                    itemStmt.setNull(6, java.sql.Types.VARCHAR); // no author for magazines
                    itemStmt.setNull(7, java.sql.Types.VARCHAR); // no genre for magazines
                    itemStmt.setInt(8, magazine.getIssueNumber());
                }

                itemStmt.setBoolean(9, item.isBorrowed());

                // for borrowed items, get the email of the user who borrowed the item
                if (item.isBorrowed()) {
                    String borrowedByEmail = null;
                    for (User user : users) {
                        if (user.getBorrowedItems().contains(item)) {
                            borrowedByEmail = user.getEmail();
                            break;
                        }
                    }
                    itemStmt.setString(10, borrowedByEmail);
                } else {
                    itemStmt.setString(10, null);
                }

                itemStmt.executeUpdate();
            }

            conn.commit(); // commit only once
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback if there is an error
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
            e.printStackTrace(); // Print the error stack trace for debugging
        } finally {
            // Always close the connection to avoid resource leaks
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restore auto-commit mode
                    conn.close();
                } catch (SQLException closeException) {
                    closeException.printStackTrace();
                }
            }
        }


    }
}
