package net.codejava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateMySqlDb {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "codejavadb";
        String username = "root";
        String password = "Licet@123";  // Using the password from your application.properties
        
        try {
            System.out.println("Connecting to MySQL server...");
            
            // Explicitly load the MySQL driver
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("MySQL JDBC Driver loaded successfully!");
            } catch (ClassNotFoundException e) {
                System.out.println("MySQL JDBC Driver not found!");
                e.printStackTrace();
                return;
            }
            
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to MySQL successfully!");
            
            Statement statement = connection.createStatement();
            
            // Create database if it doesn't exist
            System.out.println("Creating database " + dbName + " if it doesn't exist...");
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            System.out.println("Database created or already exists!");
            
            // Grant all privileges to the user
            System.out.println("Granting privileges to user " + username + "...");
            statement.executeUpdate("GRANT ALL PRIVILEGES ON " + dbName + ".* TO '" + username + "'@'localhost'");
            statement.executeUpdate("FLUSH PRIVILEGES");
            System.out.println("Privileges granted successfully!");
            
            // Connect to the new database to verify it works
            Connection dbConnection = DriverManager.getConnection(url + dbName, username, password);
            System.out.println("Successfully connected to database: " + dbName);
            
            // Close connections
            dbConnection.close();
            connection.close();
            System.out.println("Database setup complete!");
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
    }
}