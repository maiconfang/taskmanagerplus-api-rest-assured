package com.maif.taskmanagerplus_api_rest_assured.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigLoader is a utility class responsible for loading configuration properties
 * from the application-test.properties file located in the src/test/resources directory.
 * 
 * This class initializes a Properties object upon class loading and loads the properties
 * file using a FileInputStream. It provides methods to retrieve specific configuration
 * values such as database URL, username, password, and driver class name.
 * 
 * Usage:
 * - Call getDbUrl(), getUsername(), getPassword(), or getDriverClassName() to obtain
 *   the respective configuration values from the loaded properties.
 * 
 * Note:
 * - This class assumes the application-test.properties file exists in the expected path.
 * - It handles IOException by printing the stack trace. You may customize error handling
 *   based on your application's requirements, such as throwing an exception or setting
 *   default values.
 * 
 * Example:
 * ```
 * String dbUrl = ConfigLoader.getDbUrl();
 * String username = ConfigLoader.getUsername();
 * String password = ConfigLoader.getPassword();
 * String driverClassName = ConfigLoader.getDriverClassName();
 * ```
 * 
 * Author: Maicon Fang
 * Date: 2024-06-19
 */

public class ConfigLoader {

    private static Properties properties;

    static {
        properties = new Properties();
        try {
            // Load the application-test.properties file
            properties.load(new FileInputStream("src/test/resources/application-test.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., throw an exception or set default values)
        }
    }

    /**
     * Retrieves the database URL from the loaded properties.
     * 
     * @return The database URL configured in application-test.properties
     */
    public static String getDbUrl() {
        return properties.getProperty("spring.datasource.url");
    }

    /**
     * Retrieves the database username from the loaded properties.
     * 
     * @return The database username configured in application-test.properties
     */
    public static String getUsername() {
        return properties.getProperty("spring.datasource.username");
    }

    /**
     * Retrieves the database password from the loaded properties.
     * 
     * @return The database password configured in application-test.properties
     */
    public static String getPassword() {
        return properties.getProperty("spring.datasource.password");
    }
    
    /**
     * Retrieves the JDBC driver class name from the loaded properties.
     * 
     * @return The JDBC driver class name configured in application-test.properties
     */
    public static String getDriverClassName() {
        return properties.getProperty("spring.datasource.driver-class-name");
    }
}
