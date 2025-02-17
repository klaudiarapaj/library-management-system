package model;

import exception.ItemNotAvailableException;
import exception.MaxBorrowLimitException;

import java.util.Scanner;
// interface defining the main Library actions that can be taken by a simple user
public interface LibraryActions {

    // method called from the user menu to borrow an item
    void borrowItem(User user, String title) throws ItemNotAvailableException, MaxBorrowLimitException;

    // method called from the user menu to return an item
    void returnItem(User user, String title) throws ItemNotAvailableException;
}
