package tests;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> books;  // list to store books

    public Library() {
        books = new ArrayList<>();
    }

    // add a new book to the library
    public void addBook(Book book) {
        books.add(book);
    }

    //search for a book by title
    public Book searchBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;  // return null if the book is not found
    }

    // method to borrow a book
    public String borrowBook(String title) {
        Book book = searchBook(title);
        if (book == null) {
            return "Book not found.";
        } else if (book.isBorrowed()) {
            return "Book is already borrowed.";
        } else {
            book.setBorrowed(true);  // mark the book as borrowed
            return "Book borrowed successfully.";
        }
    }
}

