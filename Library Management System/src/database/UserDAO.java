package database;
import model.LibraryItem;
import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// class handles the logic behind updating the database once a INSERT/UPDATE/DELETE operation occurs
//this ensures real time synchronization and up to date state with anyone working with the database
public class UserDAO {
    // update the database after an item is borrowed
    public static void borrowItem(User user, LibraryItem item) {
        String sql = "UPDATE library_items SET borrowed_status = ?, borrowed_by = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Reflect the item's current borrowed status (true after borrowItem() is called)
            stmt.setBoolean(1, item.isBorrowed());
            stmt.setString(2, user.getEmail());
            stmt.setInt(3, item.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // update the database after an item is returned
    public static void returnItem(LibraryItem item) {
        String sql = "UPDATE library_items SET borrowed_status = ?, borrowed_by = NULL WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Reflect the item's current borrowed status (false after returnItem() is called)
            stmt.setBoolean(1, item.isBorrowed());
            stmt.setInt(2, item.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}