# library-management-system

Environment specifications and configurations
IDE: IntelliJ IDEA
JDK Version: JDK 21 (to avoid compatibility issues)
Dependencies:
•	mysql-connector-j-9.1.0
•	JUnit 5.8.1

Database Setup
1. Start MySQL Database in XAMPP
Make sure your MySQL server is running in XAMPP.
Use MySQL Workbench to execute the sql_script.sql script provided in the project.

2. Database Structure
The script will create the following:
Database: Library
Tables:
•	Users (stores user data)
•	Library_Items (stores books and magazine information)
It will also populate these tables with some sample data, including one admin user and one normal user.

Configuration
1. Database Credentials:
Open database/DatabaseConnection.java.
Go to lines 14-15 and ensure the database credentials match your local MySQL configuration (username and password).
2. Execution Instructions:
To run the program, navigate to main/Main.java and execute the program.

User Login Details
Admin Login:
•	Email: admin@gmail.com
Simple User Login:
•	Email: test@gmail.com
Register New User:
•	Provide a unique email and a name for registration.
•	After successful registration, you can log in with the new credentials.


Program Workflow
Role-Based Logic
User Roles:
The program differentiates between Admin and User roles, each having access to different features and menus.

Data Loading and Persistence
On startup, the program loads the data from the database into memory (as lists).
When the program terminates, a transaction is committed to update the database, ensuring it stays in sync with the application's current state. If any issues arise, the transaction is rolled back (this logic is handled in database/DatabaseConnection.java).

Database Operations
CRUD Operations:
INSERT, UPDATE, and DELETE operations update the database in real-time. These operations are handled by database/UserDAO.java and database/AdminDAO.java.

Thread Scheduler
•	Auto Refresh
A thread scheduler is implemented to refresh the data every 1 minute. This ensures that the program is always up-to-date with any changes made to the database externally by other threads.
•	Auto Save
The system automatically saves the data every 5 minutes to maintain synchronization between the application's state and the database. The thread scheduling and synchronization are handled by service/ThreadScheduler.java.

Project Structure
The project follows the MVC architecture: 
Models handle core business logic, including data structures for users and library items, as well as borrowing/returning operations.  
Controllers manage interactions between the views and models, ensuring proper logic execution for both users and admins.  
Views are Swing-based user interfaces that provide a user-friendly experience.

Features
User Features:
•	Borrow an Item: Borrow available books and magazines.
•	Return an Item: Return borrowed items.
•	View Borrowed Items: Display a list of items currently borrowed by the user logged.
•	View All Items: Browse the entire catalog of books and magazines.
•	Search for an Item: Search the catalog based on title.


Admin Features:
•	Add an Item: Add new books or magazines to the catalog.
•	View All Items: View all items in the library.
•	View Borrowed Items: View a list of borrowed items.
•	View Borrowed Items per User: See which items each user has borrowed.
•	Change Item Price: Update the price of any item.
•	Search for an Item: Search for items based on title.
•	Delete a User: Remove a user from the system.
•	Display All Users: View a list of all users (normal and admin).

Exception Handling and Validation
The program makes use of custom exceptions to handle unwanted scenarios, such as attempting to borrow an already borrowed book, exceeding borrowing limit or invalid user input. Input validation ensures that users cannot perform illegal actions (e.g., duplicate registrations, invalid data input).

Some Main Design Patterns
1. Functional Programming:
Functional programming techniques were utilized in the project to simplify operations on collections of objects. This made the code more concise, readable, and efficient. (Using Stream to filter and process lists (e.g., findItemByTitle method in LibraryItem)).
2. Delegation Pattern:
It was employed to separate concerns in operations like borrowing and returning items. This allows for more modular, clean, and maintainable code by delegating specific operations (e.g., user.borrowItem(), user.returnItem()) to the relevant classes. (The BookBorrowingHandler class delegates the logic of borrowing and returning books to the User class).
3. Observer Pattern:
It was implemented for some admin operations such as changing the price of an item or deleting a user. This pattern is used similarly to a notification system where listeners (observers) get notified when specific events occur. (When an admin updates an item’s price, a message is displayed on the bottom of the screen, indicating that observers are notified of the change).


Written and developed by Klaudia Rapaj for academical purposes
