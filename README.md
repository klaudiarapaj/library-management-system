# library-management-system

## Environment specifications and configurations

**IDE**: IntelliJ IDEA  
**JDK Version**: JDK 21 (to avoid compatibility issues)  
**Dependencies**:  
- `mysql-connector-j-9.1.0`  
- `JUnit 5.8.1`

## Database Setup

### 1. Start MySQL Database in XAMPP
Make sure your MySQL server is running in XAMPP.  
Use **MySQL Workbench** to execute the `sql_script.sql` script provided in the project.

### 2. Database Structure
The script will create the following:

- **Database**: `Library`
- **Tables**:
  - `Users` (stores user data)
  - `Library_Items` (stores books and magazine information)

It will also populate these tables with some sample data, including one admin user and one normal user.

## Configuration

### 1. Database Credentials
Open `database/DatabaseConnection.java`.  
Go to lines **14–15** and ensure the database credentials match your local MySQL configuration (username and password).

### 2. Execution Instructions
To run the program, navigate to `main/Main.java` and execute the program.

## User Login Details

**Admin Login**:  
- Email: `admin@gmail.com`

**Simple User Login**:  
- Email: `test@gmail.com`

**Register New User**:  
- Provide a unique email and a name for registration.  
- After successful registration, you can log in with the new credentials.

---

**Developed by Klaudia Rapaj for academic purposes.**
