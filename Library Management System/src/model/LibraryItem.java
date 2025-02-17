package model;
import view.Observer;
import java.util.ArrayList;
import java.util.List;

// the parent and fundamental class for the library items, abstract class
public abstract class LibraryItem implements Subject {   // abstraction, decided on abstract because it doesn't make sense to create an object out of LibraryItem as it's not a specific item (i.e. book/magazine)
    private int id; // encapsulating fields
    private final String title;
    private final int year;
    protected double price;
    private boolean borrowedStatus; // mutable variable
    private static List<LibraryItem> items = new ArrayList<>(); // static List that stores references of the class instances
    private List<Observer> observers;

    // parameterized constructor
    public LibraryItem(int id, String title, int year, double price) {
        this.id =id; // incrementing the ID as in a database-like context
        this.title = title;
        this.year = year;
        this.price = price;
        this.borrowedStatus = false; // borrowed status set to false by default for all newly created items
        items.add(this); // add the new instance to the static list
        this.observers = new ArrayList<>();
    }

    // method to display details of the library item
    public void displayDetails() { // displays the main info related to the library item
        System.out.println("Id: " + id + " \nTitle:  " + title + "\nYear:  " + year + "\nPrice: " + price + "$");
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
    }

    public boolean isBorrowed() { // controlled access for borrowedStatus
        return borrowedStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBorrowed(boolean borrowedStatus) {
        this.borrowedStatus = borrowedStatus;
    }

    public void borrowItem() {
        if (borrowedStatus)
            throw new IllegalStateException("Sorry, this item is borrowed :(");
        else{
            borrowedStatus = true; // setting the item as borrowed
            notifyObservers("📚 Book was Borrowed: " + title);}

    }

    public void returnItem() {
        if (!borrowedStatus)
            throw new IllegalStateException("Mistake? Item is not currently borrowed.");
        else {borrowedStatus = false; // setting the item as available
            notifyObservers("📚 Book Returned: " + title);}
    }

    public void setPrice(double newPrice) {
        this.price = newPrice;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

}
