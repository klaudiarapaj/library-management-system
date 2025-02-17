package view;

import model.*;
import controller.AdminController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

//GUI class to handle the admin menu
public class AdminView {
    private JFrame frame;
    private static AdminController controller;
    private JTextArea notificationArea;  // area to display notifications
    private AdminNotificationSystem adminNotificationSystem;

    public AdminView(AdminController controller,  Admin admin) {
        AdminView.controller = controller;
        initializeUI(admin);
    }

    void initializeUI(Admin admin) {
        frame = new JFrame("Admin Menu");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);


        // main Menu Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new GridLayout(5, 1, 10, 10));
        JButton itemUtilitiesButton = createStyledButton("Item Utilities");
        JButton itemStatisticsButton = createStyledButton("Item Search");
        JButton userUtilitiesButton = createStyledButton("User Utilities");
        JButton logoutButton = createStyledButton("Logout");

        notificationArea = new JTextArea(6, 30);
        notificationArea.setEditable(false);  // make read-only
        JScrollPane scrollPane = new JScrollPane(notificationArea);

        mainPanel.add(itemUtilitiesButton);
        mainPanel.add(itemStatisticsButton);
        mainPanel.add(userUtilitiesButton);
        mainPanel.add(logoutButton);
        mainPanel.add(scrollPane);

        frame.add(mainPanel);

        adminNotificationSystem = new AdminNotificationSystem(notificationArea);
        admin.addObserver(adminNotificationSystem);  // register the observer

        // button actions
        itemUtilitiesButton.addActionListener(e -> openItemUtilities());
        itemStatisticsButton.addActionListener(e -> openItemSearch());
        userUtilitiesButton.addActionListener(e -> openUserUtilities());
        logoutButton.addActionListener(e -> {
            if (controller.logout()) {
                JOptionPane.showMessageDialog(frame, "You have been logged out. Goodbye!");
                controller.logout();
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(139, 0, 0));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    //item utilities menu
    private void openItemUtilities() {
        JFrame itemMenu = new JFrame("Item Utilities");
        itemMenu.setSize(400, 300);
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBackground(Color.WHITE);

        JButton addItemButton = createStyledButton("Add Item");
        JButton displayItemsButton = createStyledButton("Display All Items");
        JButton displayBorrowedButton = createStyledButton("Display All Borrowed Items");
        JButton borrowedByUserButton = createStyledButton("Display Borrowed Items for User");
        JButton changePriceButton = createStyledButton("Change Item Price");
        JButton backButton = createStyledButton("Back");

        panel.add(addItemButton);
        panel.add(displayItemsButton);
        panel.add(displayBorrowedButton);
        panel.add(borrowedByUserButton);
        panel.add(changePriceButton);
        panel.add(backButton);

        itemMenu.add(panel);
        itemMenu.setLocationRelativeTo(null);
        itemMenu.setVisible(true);

        addItemButton.addActionListener(e -> openAddItemMenu());
        displayItemsButton.addActionListener(e -> displayAllItems());
        displayBorrowedButton.addActionListener(e -> displayAllBorrowedItems());
        borrowedByUserButton.addActionListener(e -> {
            String email = promptInput("Enter User Email:");
            if (email == null || email.trim().isEmpty()) {
                return;
            }
            displayUserBorrowedItems(email);
        });
        changePriceButton.addActionListener(e -> openChangePriceMenu());
        backButton.addActionListener(e -> itemMenu.dispose());
    }

    // item search menu
    private void openItemSearch() {
        String searchQuery = JOptionPane.showInputDialog(frame, "Enter title to search:");
        if (searchQuery != null) {
            try {
                List<LibraryItem> results = controller.searchItemByTitle(searchQuery);
                if (results.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No results found.");
                    return;
                }

                StringBuilder sb = new StringBuilder("Search Results:\n\n");
                for (LibraryItem item : results) {
                    sb.append("Title: ").append(item.getTitle()).append("\n");
                    sb.append("Year: ").append(item.getYear()).append("\n");
                    sb.append("Price: $").append(item.getPrice()).append("\n");

                    if (item instanceof Book) {
                        Book book = (Book) item;
                        sb.append("Author: ").append(book.getAuthor()).append("\n");
                        sb.append("Genre: ").append(book.getGenre()).append("\n");
                    }

                    else if (item instanceof Magazine) {
                        Magazine magazine = (Magazine) item;
                        sb.append("Issue Number: ").append(magazine.getIssueNumber()).append("\n");
                    }

                    sb.append("--------------------------\n");
                }

                JOptionPane.showMessageDialog(frame, sb.toString());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, e.getMessage());
            }
        }
    }

    private void openChangePriceMenu() {
        JFrame changePriceFrame = new JFrame("Change Item Price");
        changePriceFrame.setSize(400, 200);
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(Color.WHITE);

        JLabel itemLabel = new JLabel("Select Item:");
        JComboBox<String> itemDropdown = new JComboBox<>();
        JLabel oldPriceLabel = new JLabel("Old Price:");
        JTextField oldPriceField = new JTextField();
        oldPriceField.setEditable(false);
        JLabel newPriceLabel = new JLabel("New Price:");
        JTextField newPriceField = new JTextField();

        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");

        panel.add(itemLabel);
        panel.add(itemDropdown);
        panel.add(oldPriceLabel);
        panel.add(oldPriceField);
        panel.add(newPriceLabel);
        panel.add(newPriceField);
        panel.add(saveButton);
        panel.add(cancelButton);

        changePriceFrame.add(panel);

        for (LibraryItem item : controller.getAllItems()) {
            itemDropdown.addItem(item.getId() + " - " + item.getTitle());
        }

        itemDropdown.addActionListener(e -> {
            String selected = (String) itemDropdown.getSelectedItem();
            if (selected != null) {
                int itemId = Integer.parseInt(selected.split(" - ")[0]);
                LibraryItem selectedItem = controller.findItemById(itemId);
                if (selectedItem != null) {
                    oldPriceField.setText(String.valueOf(selectedItem.getPrice()));
                }
            }
        });

        saveButton.addActionListener(e -> {
            try {
                String selected = (String) itemDropdown.getSelectedItem();
                if (selected != null) {
                    int itemId = Integer.parseInt(selected.split(" - ")[0]);
                    double newPrice = Double.parseDouble(newPriceField.getText());

                    if (newPrice < 0) {
                        JOptionPane.showMessageDialog(changePriceFrame, "Price cannot be negative. Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    controller.changeItemPrice(itemId, newPrice);
                    JOptionPane.showMessageDialog(changePriceFrame, "Price updated successfully!");
                    changePriceFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(changePriceFrame, "Please select an item.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(changePriceFrame, "Invalid price entered. Please try again.");
            }
        });

        cancelButton.addActionListener(e -> changePriceFrame.dispose());
        changePriceFrame.setLocationRelativeTo(null);

        changePriceFrame.setVisible(true);
    }

    private void openAddItemMenu() {
        JFrame addItemFrame = new JFrame("Add Item");
        addItemFrame.setSize(400, 300);
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        addItemFrame.setLocationRelativeTo(null);


        JLabel typeLabel = new JLabel("Select Item Type:");
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Book", "Magazine"});
        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField();
        JLabel yearLabel = new JLabel("Year:");
        JTextField yearField = new JTextField();
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(); //extra fields because Book has more fields than Magazine
        JLabel extraLabel1 = new JLabel();
        JTextField extraField1 = new JTextField();
        JLabel extraLabel2 = new JLabel();
        JTextField extraField2 = new JTextField();

        JButton saveButton = createStyledButton("Save");
        JButton backButton = createStyledButton("Back");

        panel.add(typeLabel);
        panel.add(typeComboBox);
        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(yearLabel);
        panel.add(yearField);
        panel.add(priceLabel);
        panel.add(priceField);
        panel.add(extraLabel1);
        panel.add(extraField1);
        panel.add(extraLabel2);
        panel.add(extraField2);
        panel.add(saveButton);
        panel.add(backButton);


        typeComboBox.setSelectedIndex(0);
        extraLabel1.setText("Author:");
        extraLabel2.setText("Genre:");

        // update labels based on selected item type
        typeComboBox.addActionListener(e -> {
            String selectedType = (String) typeComboBox.getSelectedItem();
            if ("Book".equals(selectedType)) {
                extraLabel1.setText("Author:");
                extraLabel2.setText("Genre:");
            } else if ("Magazine".equals(selectedType)) {
                extraLabel1.setText("Issue Number:");
                extraLabel2.setText("");
                extraField2.setVisible(false); // hide the second extra field for magazines

            }
            panel.revalidate();
            panel.repaint();
        });

        // save Button Action
        saveButton.addActionListener(e -> {
            try {
                // get the inputs and parse them
                String title = titleField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());

                // validate required fields
                if (title.isEmpty() || year <= 0 || price <= 0) {
                    JOptionPane.showMessageDialog(addItemFrame, "Please fill out all fields correctly.");
                    return;
                }

                // add Book or Magazine based on type selection
                String selectedType = (String) typeComboBox.getSelectedItem();
                if ("Book".equals(selectedType)) {
                    String author = extraField1.getText().trim();
                    String genre = extraField2.getText().trim();

                    // validate Book-specific fields
                    if (author.isEmpty() || genre.isEmpty()) {
                        JOptionPane.showMessageDialog(addItemFrame, "Please provide both author and genre for the book.");
                        return;
                    }

                    controller.addBook(title, year, price, author, genre);
                    JOptionPane.showMessageDialog(addItemFrame, "Book added successfully!");

                } else if ("Magazine".equals(selectedType)) {
                    int issueNumber = Integer.parseInt(extraField1.getText().trim());

                    controller.addMagazine(title, year, price, issueNumber);
                    JOptionPane.showMessageDialog(addItemFrame, "Magazine added successfully!");
                }

                // clear fields for adding another item
                titleField.setText("");
                yearField.setText("");
                priceField.setText("");
                extraField1.setText("");
                extraField2.setText("");
                typeComboBox.setSelectedIndex(0);

            } catch (NumberFormatException ex) {
                // catch invalid numeric entries
                JOptionPane.showMessageDialog(addItemFrame, "Please enter valid numbers for the int fields.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(addItemFrame, "Error: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> addItemFrame.dispose());

        addItemFrame.add(panel);
        addItemFrame.setVisible(true);
    }

    //user utilities menu
    private void openUserUtilities() {
        JFrame userMenu = new JFrame("User Utilities");
        userMenu.setSize(400, 300);
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBackground(Color.WHITE);

        JButton deleteUserButton = createStyledButton("Delete User");
        JButton displayUsersButton = createStyledButton("Display All Users");
        JButton backButton = createStyledButton("Back");

        panel.add(deleteUserButton);
        panel.add(displayUsersButton);
        panel.add(backButton);

        userMenu.add(panel);
        userMenu.setLocationRelativeTo(null);
        userMenu.setVisible(true);

        deleteUserButton.addActionListener(e -> {
            String email = promptInput("Enter User Email to Delete:");

            if (email == null || email.trim().isEmpty()) {
                JOptionPane.showMessageDialog(userMenu, "No email entered. User deletion canceled.", "Cancel", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // call the controller and check if the deletion was successful
            boolean isDeleted = controller.deleteUser(email);

            // show appropriate message based on the result
            if (isDeleted) {
                JOptionPane.showMessageDialog(userMenu, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(userMenu, "User with email " + email + " does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        displayUsersButton.addActionListener(e -> displayAllUsers());
        backButton.addActionListener(e -> userMenu.dispose());
    }

    private String promptInput(String message) {
        return JOptionPane.showInputDialog(frame, message);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    private void displayAllItems() {
        List<LibraryItem> allItems = controller.getAllItems();

        String[] columnNames = {"ID", "Type", "Title", "Year", "Price", "Author", "Genre", "Issue Number", "Availability"};
        Object[][] data = new Object[allItems.size()][9];

        for (int i = 0; i < allItems.size(); i++) {
            LibraryItem item = allItems.get(i);
            data[i][0] = item.getId();

            data[i][1] = item instanceof model.Book ? "Book" : "Magazine";
            data[i][2] = item.getTitle();     // Title
            data[i][3] = item.getYear();      // Year
            data[i][4] = item.getPrice();     // Price

            if (item instanceof model.Book) {
                model.Book book = (model.Book) item;
                data[i][5] = book.getAuthor();   // Author for Books
                data[i][6] = book.getGenre();    // Genre for Books
                data[i][7] = "";              // No issue number for Books
            } else if (item instanceof model.Magazine) {
                model.Magazine magazine = (model.Magazine) item;
                data[i][5] = "";                 // No author for Magazines
                data[i][6] = "";                 // No genre for Magazines
                data[i][7] = magazine.getIssueNumber(); // Issue Number for Magazines
            }

            // add availability status
            data[i][8] = item.isBorrowed() ? "Borrowed" : "Available";
        }

        // create a table-like structure
        JTable itemTable = new JTable(data, columnNames);
        itemTable.setBackground(new Color(245, 245, 245));
        itemTable.setFont(new Font("Arial", Font.PLAIN, 12));
        itemTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(itemTable);
        JFrame tableFrame = new JFrame("All Library Items");
        tableFrame.setSize(1000, 400);
        tableFrame.add(scrollPane);
        tableFrame.getContentPane().setBackground(new Color(220, 220, 220));
        tableFrame.setLocationRelativeTo(null);
        tableFrame.setVisible(true);
    }


    private void displayAllBorrowedItems() {
        List<LibraryItem> borrowedItems = controller.getAllBorrowedItems();
        if (borrowedItems.isEmpty()) {
            showMessage("No borrowed items across all users.");
        } else {
            StringBuilder display = new StringBuilder("Borrowed Items:\n");
            for (LibraryItem item : borrowedItems) {
                display.append(item.getTitle()).append("\n");
            }
            showMessage(display.toString());
        }
    }

    private void displayUserBorrowedItems(String email) {
        if (email == null || email.trim().isEmpty()) {
            return;
        }

        User user = controller.getUserByEmail(email);
        if (user == null) {
            showMessage("No user found with email: " + email);
            return;
        }

        List<LibraryItem> borrowedItems = controller.getUserBorrowedItems(email);
        StringBuilder display = new StringBuilder("User " + user.getName() + " borrowed:\n");

        if (borrowedItems.isEmpty()) {
            display.append("No borrowed items.");
        } else {
            for (LibraryItem item : borrowedItems) {
                display.append(item.getTitle()).append("\n");
            }
        }

        showMessage(display.toString());
    }

    private void displayAllUsers() {
        List<User> users = controller.getAllUsers();

        if (users.isEmpty()) {
            showMessage("No users registered.");
            return;
        }

        String[] columnNames = {"ID", "Name", "Email", "Borrowed Items"};

        Object[][] data = new Object[users.size()][4];

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i][0] = user.getId();
            data[i][1] = user.getName();
            data[i][2] = user.getEmail();

            // get borrowed item titles
            List<LibraryItem> borrowedItems = user.getBorrowedItems();
            if (borrowedItems.isEmpty()) {
                data[i][3] = "None";
            } else {
                data[i][3] = borrowedItems.stream()
                        .map(LibraryItem::getTitle)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("None");
            }
        }

        // create a table-like structure
        JTable userTable = new JTable(data, columnNames);
        userTable.setBackground(new Color(245, 245, 245));
        userTable.setFont(new Font("Arial", Font.PLAIN, 12));
        userTable.setRowHeight(25);

        // wrap table in a scroll pane
        JScrollPane scrollPane = new JScrollPane(userTable);
        JFrame tableFrame = new JFrame("All Users");
        tableFrame.setSize(600, 400);
        tableFrame.add(scrollPane);
        tableFrame.getContentPane().setBackground(new Color(220, 220, 220));
        tableFrame.setLocationRelativeTo(null);
        tableFrame.setVisible(true);
    }


}

