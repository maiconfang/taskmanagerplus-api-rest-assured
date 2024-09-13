package com.maif.taskmanagerplus_api_rest_assured.tests.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;

import com.maif.taskmanagerplus_api_rest_assured.config.ConfigLoader;

/**
 * Utility class for performing database operations related to tasks.
 * This class provides methods to insert and delete tasks from the database.
 * 
 * Author: Maicon Fang
 * Date: 2024-06-19
 */

public class DataBaseInsertUtil {

    private static final String dbUrl = ConfigLoader.getDbUrl();
    private static final String username = ConfigLoader.getUsername();
    private static final String password = ConfigLoader.getPassword();
    private static final String driverClassName = ConfigLoader.getDriverClassName();

    /**
     * Inserts a new task into the 'task' table and returns the ID of the inserted task.
     */
    public static int insertTask(String title, String description, LocalDate dueDate, boolean completed) {
        int generatedId = -1;
        try {
            // Register the JDBC driver
            Class.forName(driverClassName);

            // Establish the connection to the database
            try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
            	
                // Convert LocalDate to java.sql.Date
                Date sqlDueDate = Date.valueOf(dueDate);
            	
                // Call insertEntity with converted date
                generatedId = insertEntity(connection, "task", 
                                          "title", title, 
                                          "description", description, 
                                          "due_date", sqlDueDate, 
                                          "completed", completed);

//                System.out.println("Inserted new task with ID: " + generatedId);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }
    
    
    /**
     * Deletes a task from the 'task' table based on the given task ID.
     *
     * @param taskId The ID of the task to delete.
     * @return true if the task was successfully deleted, false otherwise.
     */
    public static boolean deleteTask(int taskId) {
        try {
            // Register the JDBC driver
            Class.forName(driverClassName);

            // Establish the connection to the database
            try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
                // Create the SQL statement for deletion
                String sql = "DELETE FROM task WHERE id = ?";

                // Prepare the statement
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    // Set the task ID parameter
                    statement.setInt(1, taskId);

                    // Execute the delete statement
                    int rowsDeleted = statement.executeUpdate();

                    // Check if deletion was successful
                    return rowsDeleted > 0;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return false; // Return false by default if deletion fails
    }
    
    
    
    /**
     * Inserts a new province into the 'provinces' table and returns the ID of the inserted province.
     */
    public static int insertProvince(String name, String abbreviation) {
        int generatedId = -1;
        try {
            // Register the JDBC driver
            Class.forName(driverClassName);

            // Establish the connection to the database
            try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
                // Call insertEntity with the new fields
                generatedId = insertEntity(connection, "province",
                        "name", name,
                        "abbreviation", abbreviation);
                
                // Print a message if needed
                // System.out.println("Inserted new province with ID: " + generatedId);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }
    
    
    /**
     * Deletes a province from the 'provinces' table based on the given province ID.
     *
     * @param provinceId The ID of the province to delete.
     * @return true if the province was successfully deleted, false otherwise.
     */
    public static boolean deleteProvince(int provinceId) {
        try {
            // Register the JDBC driver
            Class.forName(driverClassName);

            // Establish the connection to the database
            try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
                // Create the SQL statement for deletion
                String sql = "DELETE FROM province WHERE id = ?";

                // Prepare the statement
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    // Set the province ID parameter
                    statement.setInt(1, provinceId);

                    // Execute the delete statement
                    int rowsDeleted = statement.executeUpdate();

                    // Check if deletion was successful
                    return rowsDeleted > 0;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return false; // Return false by default if deletion fails
    }

    
    /**
     * Deletes a user from the 'usserr' table based on the given user ID.
     *
     * @param userId The ID of the user to delete.
     * @return true if the user was successfully deleted, false otherwise.
     */
    public static boolean deleteUser(int userId) {
        try {
            // Register the JDBC driver
            Class.forName(driverClassName);

            // Establish the connection to the database
            try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
                // Create the SQL statement for deletion
                String sql = "DELETE FROM usserr WHERE id = ?";

                // Prepare the statement
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    // Set the user ID parameter
                    statement.setInt(1, userId);

                    // Execute the delete statement
                    int rowsDeleted = statement.executeUpdate();

                    // Check if deletion was successful
                    return rowsDeleted > 0;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return false; // Return false by default if deletion fails
    }


    /**
     * Inserts a new user into the 'usserr' table and returns the ID of the inserted usserr.
     */
    public static int insertUser(String userName, String userEmail, String userPassword, Timestamp dtCreate, Timestamp dtUpdate) {
        int generatedId = -1;
        try {
            // Register the JDBC driver
            Class.forName(driverClassName);

            // Establish the connection to the database
            try (Connection connection = DriverManager.getConnection(dbUrl, username, password)) {
                // Call insertEntity with the new fields
                generatedId = insertEntity(connection, "usserr",
                        "name", userName,
                        "email", userEmail,
                        "password", userPassword,
                        "dt_create", dtCreate,
                        "dt_update", dtUpdate);
                
                // Print a message if needed
                // System.out.println("Inserted new usserr with ID: " + generatedId);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    /**
     * Inserts a new entity into the specified table with the given columns and values.
     * Returns the generated ID of the inserted entity.
     */
    private static int insertEntity(Connection connection, String tableName, Object... columnsAndValues) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO ").append(tableName).append(" (");

        for (int i = 0; i < columnsAndValues.length; i += 2) {
            if (i > 0) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append(columnsAndValues[i]);
        }

        sqlBuilder.append(") VALUES (");

        for (int i = 0; i < columnsAndValues.length; i += 2) {
            if (i > 0) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append("?");
        }

        sqlBuilder.append(")");

        try (PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString(), Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < columnsAndValues.length; i += 2) {
//                String columnName = (String) columnsAndValues[i];
                Object value = columnsAndValues[i + 1];

                if (value instanceof String) {
                    statement.setString(i / 2 + 1, (String) value);
                } else if (value instanceof Timestamp) {
                    statement.setTimestamp(i / 2 + 1, (Timestamp) value);
                } else if (value instanceof Boolean) {
                    statement.setBoolean(i / 2 + 1, (Boolean) value);
                } else if (value instanceof Date) {
                    statement.setTimestamp(i / 2 + 1, new Timestamp(((Date) value).getTime()));
                }
            }

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }

        return -1; // Return -1 if no ID was generated
    }
}
