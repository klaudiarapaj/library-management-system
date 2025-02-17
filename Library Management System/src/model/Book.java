package model;

// book class is a subclass of the libraryItem class, here are defined the constructor chaining, the display method, and getters and setters(price)
public class Book extends LibraryItem{   // inheritance
    private final String author; // encapsulating all fields
    private final String genre;

    // parameterized constructor
    public Book(int id,String title, int year, double price, String author, String genre){
        super(id, title, year, price);
        this.author=author;
        this.genre=genre;
    }

    @Override
    public void displayDetails(){  // polymorphism
        super.displayDetails(); // inherits the method from the parent class, adding the new variables
        System.out.println("Author:  "+author+"\nGenre:  "+genre);
        System.out.println();
    }

    public String getAuthor(){
        return author;
    }

    public String getGenre(){
        return genre;
    }

    public void setPrice(double price) { //method to modify the price because its not final and can change (by the admin)
        this.price = price;
    }
}