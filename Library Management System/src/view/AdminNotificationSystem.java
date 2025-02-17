package view;
import javax.swing.*;

public class AdminNotificationSystem implements Observer {
    private JTextArea notificationArea;

    public AdminNotificationSystem(JTextArea notificationArea) {
        this.notificationArea = notificationArea;
    }

    @Override
    public void update(String message) {
        notificationArea.append(message + "\n");
    }
}
