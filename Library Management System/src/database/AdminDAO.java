package database;
import model.Book;
import model.Magazine;
import java.sql.*;

// class handles the logic behind updating the database once a INSERT/UPDATE/DELETE operation occurs
//this ensures real time synchronization and up to date state with anyone working with the database
public class AdminDAO {

    // update the price of a library item in the database
    public static void changeItemPrice(int itemId, double newPrice) {
        String sql = "UPDATE library_items SET price = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newPrice);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // delete a user from the database
    public static void deleteUser(String email) {
        String sql = "DELETE FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // insert a book
    public static int insertBook(Book book) {
        String sql = "INSERT INTO library_items (title, year, price, type, author, genre) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getYear());
            stmt.setDouble(3, book.getPrice());
            stmt.setString(4, "book");
            stmt.setString(5, book.getAuthor());
            stmt.setString(6, book.getGenre());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // insert a magazine
    public static int insertMagazine(Magazine magazine) {
        String sql = "INSERT INTO library_items (title, year, price, type, issue_number) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, magazine.getTitle());
            stmt.setInt(2, magazine.getYear());
            stmt.setDouble(3, magazine.getPrice());
            stmt.setString(4, "magazine");
            stmt.setInt(5, magazine.getIssueNumber());
            stmt.executeUpdate();


            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}