package com.maif.taskmanagerplus_api_rest_azure.tests.random;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.maif.taskmanagerplus_api_rest_azure.config.ConfigLoader;

/**
 * DatabaseTestUtil is a utility class designed to facilitate testing interactions with a MySQL database.
 * It demonstrates basic JDBC operations such as connecting to the database, executing SQL queries,
 * and retrieving data from a specified table.
 * 
 * It uses configuration values (database URL, username, password, and driver class) obtained from
 * ConfigLoader, ensuring flexibility and ease of maintenance. This class is particularly useful for
 * developers who need to quickly validate database connectivity or execute sample queries against
 * an existing MySQL database.
 * 
 * This class can be used as a standalone program or integrated into a testing framework to verify
 * database setup, test SQL queries, or serve as a learning tool for JDBC operations.
 * 
 * Author: Maicon Fang
 * Date: 2024-06-20
 */


public class DatabaseTestUtil {

    public static void main(String[] args) {
        String dbUrl = ConfigLoader.getDbUrl();
        String username = ConfigLoader.getUsername();
        String password = ConfigLoader.getPassword();
        String driverClassName = ConfigLoader.getDriverClassName();

        try {
            // Register the JDBC driver
            Class.forName(driverClassName);

            // Establish the connection to the database
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            
            // Create an SQL statement
            Statement statement = connection.createStatement();
            
            // Execute the SQL query
            ResultSet resultSet = statement.executeQuery("SELECT * FROM task");

            // Use the connection to execute queries, etc.
            // Example:
            // Iterate over the results
            while (resultSet.next()) {
                // Extract values from each column
                int id = resultSet.getInt("id");
                String name = resultSet.getString("title");
                String description = resultSet.getString("description");
                // Add other columns you have in the 'task' table here

                // Print values to the console
                System.out.println("ID: " + id + ", Name: " + name + ", Description: " + description);
            }

            // Close the connection when finished
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
