package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//testing scenarios to ensure different operations are handled correctly
public class test {
    private Library library;
    private Book book1;
    private Book book2;

    @BeforeEach
    public void setup() {
        library = new Library();
        book1 = new Book("Attack on Titan");
        book2 = new Book("Haikyuu");
    }

    @Test
    public void testAddBook() {
        library.addBook(book1);
        assertEquals(book1, library.searchBook("Attack on Titan"));
    }

    @Test
    public void testSearchBook() {
        library.addBook(book1);
        library.addBook(book2);
        assertNotNull(library.searchBook("Attack on Titan"));
        assertNull(library.searchBook("Unknown Book"));
    }

    @Test
    public void testBorrowBookSuccess() {
        library.addBook(book1);
        String result = library.borrowBook("Attack on Titan");
        assertEquals("Book borrowed successfully.", result);
        assertTrue(book1.isBorrowed());
    }

    @Test
    public void testBorrowBookAlreadyBorrowed() {
        library.addBook(book1);
        library.borrowBook("Attack on Titan");  //borrowing 1st time
        String result = library.borrowBook("Attack on Titan"); //2nd attempt borrowing
        assertEquals("Book is already borrowed.", result);
    }

    @Test
    public void testBorrowBookNotFound() {
        String result = library.borrowBook("Book doesnt exist");
        assertEquals("Book not found.", result);
    }
}

