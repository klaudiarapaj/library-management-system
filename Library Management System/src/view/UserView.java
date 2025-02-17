package view;

import model.Book;
import model.LibraryItem;
import controller.UserController;
import model.Magazine;

import javax.swing.*;
import java.awt.*;
import java.util.List;
//GUI class to handle the user menu
public class UserView {

    private JFrame frame;
    private static UserController controller;

    // define the colors for the theme
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color BUTTON_COLOR = new Color(139, 0, 0); // dark red color
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;

    public UserView(UserController controller) {
        UserView.controller = controller;
        initializeUI();
    }

    void initializeUI() {
        frame = new JFrame("User Menu");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        // Main Menu Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 1, 10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // create buttons with the theme applied
        JButton itemUtilitiesButton = createThemedButton("Item Utilities");
        JButton itemSearchButton = createThemedButton("Item Search");
        JButton logoutButton = createThemedButton("Logout");

        // add buttons to panel
        mainPanel.add(itemUtilitiesButton);
        mainPanel.add(itemSearchButton);
        mainPanel.add(logoutButton);

        frame.add(mainPanel);

        // button Actions
        itemUtilitiesButton.addActionListener(e -> openItemUtilities());
        itemSearchButton.addActionListener(e -> openItemSearch());
        logoutButton.addActionListener(e -> {
            if (controller.logout()) {
                JOptionPane.showMessageDialog(frame, "You have been logged out. Goodbye!");
                controller.logout();
                frame.dispose();
            }
        });

        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        frame.setVisible(true);
    }

    private JButton createThemedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setOpaque(true);
        return button;
    }

    // open Item Utilities Menu (Borrow, Return, Display Borrowed Items, Display All Items)
    private void openItemUtilities() {
        JFrame itemMenu = new JFrame("Item Utilities");
        itemMenu.setSize(400, 300);
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);

        JButton borrowButton = createThemedButton("Borrow Item");
        JButton returnButton = createThemedButton("Return Item");
        JButton displayBorrowedButton = createThemedButton("Display Borrowed Items");
        JButton displayAllItemsButton = createThemedButton("Display All Items");
        JButton backButton = createThemedButton("Back");

        panel.add(borrowButton);
        panel.add(returnButton);
        panel.add(displayBorrowedButton);
        panel.add(displayAllItemsButton);
        panel.add(backButton);

        itemMenu.add(panel);

        // button Actions
        borrowButton.addActionListener(e -> borrowItem());
        returnButton.addActionListener(e -> returnItem());
        displayBorrowedButton.addActionListener(e -> displayBorrowedItems());
        displayAllItemsButton.addActionListener(e -> displayAllItems());
        backButton.addActionListener(e -> itemMenu.dispose());

        itemMenu.getContentPane().setBackground(BACKGROUND_COLOR);
        itemMenu.setLocationRelativeTo(null);
        itemMenu.setVisible(true);
    }

    // open Item Search Menu
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

                    // If item is a book, display author and genre
                    if (item instanceof Book) {
                        Book book = (Book) item;
                        sb.append("Author: ").append(book.getAuthor()).append("\n");
                        sb.append("Genre: ").append(book.getGenre()).append("\n");
                    }
                    // If item is a magazine, display issue number
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


    // borrow Item
    private void borrowItem() {
        String itemTitle = JOptionPane.showInputDialog(frame, "Enter item title to borrow:");
        if (itemTitle != null) {
            String result = controller.borrowItem(itemTitle);
            if ("success".equals(result)) {
                JOptionPane.showMessageDialog(frame, "You've successfully borrowed " + itemTitle + ".");
            } else {
                JOptionPane.showMessageDialog(frame, result);
            }
        }
    }

    // return Item
    private void returnItem() {
        String itemTitle = JOptionPane.showInputDialog(frame, "Enter item title to return:");
        if (itemTitle != null) {
            String result = controller.returnItem(itemTitle);
            if ("success".equals(result)) {
                JOptionPane.showMessageDialog(frame, "You've successfully returned " + itemTitle + ".");
            } else {
                JOptionPane.showMessageDialog(frame, result);
            }
        }
    }

    // display borrowed items
    private void displayBorrowedItems() {
        List<LibraryItem> borrowedItems = controller.getBorrowedItems();
        StringBuilder sb = new StringBuilder();
        sb.append("Your borrowed items list:\n");

        // if there are borrowed items, append their titles
        if (borrowedItems.isEmpty()) {
            sb.append("You haven't borrowed any items.");
        } else {
            for (LibraryItem item : borrowedItems) {
                sb.append(item.getTitle()).append("\n");
            }
        }
        JOptionPane.showMessageDialog(frame, sb.toString());
    }

    private void displayAllItems() {
        List<LibraryItem> allItems = controller.getAllItems();


        String[] columnNames = {"ID", "Type", "Title", "Year", "Price", "Author", "Genre", "Issue Number", "Availability"};
        Object[][] data = new Object[allItems.size()][9]; // 9 columns now include Type

        for (int i = 0; i < allItems.size(); i++) {
            LibraryItem item = allItems.get(i);
            data[i][0] = item.getId();
            // determine type of the item (Book or Magazine)
            data[i][1] = item instanceof model.Book ? "Book" : "Magazine";
            data[i][2] = item.getTitle();     // Title
            data[i][3] = item.getYear();      // Year
            data[i][4] = item.getPrice();     // Price

            if (item instanceof model.Book) {
                model.Book book = (model.Book) item;
                data[i][5] = book.getAuthor();
                data[i][6] = book.getGenre();
                data[i][7] = "";              // No issue number for Books
            } else if (item instanceof model.Magazine) {
                model.Magazine magazine = (model.Magazine) item;
                data[i][5] = "";                 // No author for Magazines
                data[i][6] = "";                 // No genre for Magazines
                data[i][7] = magazine.getIssueNumber(); //
            }

            // add availability status
            data[i][8] = item.isBorrowed() ? "Borrowed" : "Available";
        }

        // create a table-like structure
        JTable itemTable = new JTable(data, columnNames);
        itemTable.setBackground(new Color(245, 245, 245)); // light background for table
        itemTable.setFont(new Font("Arial", Font.PLAIN, 12));
        itemTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(itemTable);
        JFrame tableFrame = new JFrame("All Library Items");
        tableFrame.setSize(1000, 400); // adjust width for the additional "Type" column
        tableFrame.add(scrollPane);
        tableFrame.getContentPane().setBackground(new Color(220, 220, 220)); // grayish background for frame
        tableFrame.setLocationRelativeTo(null);
        tableFrame.setVisible(true);
    }

}
