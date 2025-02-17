package view;

import model.User;

import javax.swing.*;
import java.awt.*;
//GUI class to handle the login/register menu
public class SystemView {
    private JFrame frame;

    public SystemView() {
        initializeMainFrame();
    }

    private void initializeMainFrame() {
        frame = new JFrame("Library Management System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 1, 10, 10));
        frame.setLocationRelativeTo(null); // Center the window

    }

    public void displayMainMenu(MainMenuListener listener) {
        frame.getContentPane().removeAll();

        // styled Buttons
        JButton registerButton = createStyledButton("Register");
        JButton loginButton = createStyledButton("Login");
        JButton exitButton = createStyledButton("Exit");

        registerButton.addActionListener(e -> listener.onRegisterSelected());
        loginButton.addActionListener(e -> listener.onLoginSelected());
        exitButton.addActionListener(e -> listener.onExitSelected());

        frame.add(registerButton);
        frame.add(loginButton);
        frame.add(exitButton);

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    public String showLoginForm() {
        JDialog loginDialog = new JDialog(frame, "Login", true);
        loginDialog.setSize(300, 200);
        loginDialog.setLayout(new GridLayout(3, 1, 10, 10));
        loginDialog.getContentPane().setBackground(Color.WHITE);
        loginDialog.setLocationRelativeTo(null);


        // components
        JTextField emailField = createStyledTextField();
        JButton loginButton = createStyledButton("Login");
        JLabel emailLabel = createStyledLabel("Enter your email:");

        loginDialog.add(emailLabel);
        loginDialog.add(emailField);
        loginDialog.add(loginButton);

        final String[] email = {null}; // store the result
        loginButton.addActionListener(e -> {
            email[0] = emailField.getText();
            loginDialog.dispose();
        });

        loginDialog.setVisible(true); // show dialog
        return email[0]; // return the user-entered email
    }

    public User showRegisterForm() {
        JDialog registerDialog = new JDialog(frame, "Register", true);
        registerDialog.setSize(300, 300);
        registerDialog.setLayout(new GridLayout(5, 1, 10, 10));
        registerDialog.getContentPane().setBackground(Color.WHITE);
        registerDialog.setLocationRelativeTo(null);


        // components
        JTextField nameField = createStyledTextField();
        JTextField emailField = createStyledTextField();
        JButton registerButton = createStyledButton("Register");
        JLabel nameLabel = createStyledLabel("Enter your name:");
        JLabel emailLabel = createStyledLabel("Enter your email:");

        registerDialog.add(nameLabel);
        registerDialog.add(nameField);
        registerDialog.add(emailLabel);
        registerDialog.add(emailField);
        registerDialog.add(registerButton);

        final User[] newUser = {null}; // store the result
        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            if (name.isBlank() || email.isBlank()) {
                JOptionPane.showMessageDialog(registerDialog, "Both fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                newUser[0] = new User(name, email);
                registerDialog.dispose();
            }
        });

        registerDialog.setVisible(true); // show dialog
        return newUser[0]; // return the created user or null if canceled
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showLoginSuccess(User user) {
        JOptionPane.showMessageDialog(frame, "Welcome, " + user.getName() + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showLoginFailure() {
        JOptionPane.showMessageDialog(frame, "Login failed. User not found.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(139, 0, 0));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return button;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return textField;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text, JLabel.LEFT);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.BLACK);
        return label;
    }

    public interface MainMenuListener {
        void onRegisterSelected();
        void onLoginSelected();
        void onExitSelected();
    }
}
