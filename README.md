

# TaskManagerPlus API Rest Assured Tests

This project is designed to test the Task Manager Plus API using the **Rest Assured** library. The tests cover various aspects of the API, including provinces, users, and tasks. 


## Key Components

### 1. **AuthUtil.java**
   - This utility class handles authentication for API requests by adding the authorization token to the headers.

### 2. **ConfigLoader.java**
   - This class is responsible for loading configuration properties (e.g., database URL, username, password) from the `application-test.properties` file.

### 3. **DTOs (Data Transfer Objects)**
   - **ProvinceDTO.java**: Contains data fields related to provinces, such as ID, name, and abbreviation.
   - **TaskDTO.java**: Contains data fields related to tasks, such as ID, title, description, due date, and completion status.

### 4. **Test Classes**
   - **ProvinceApiTest.java**: Tests the `Provinces` API endpoints.
   - **UserApiTest.java**: Tests the `Users` API endpoints.
   - **UserChangePasswordApiTest.java**: Tests the user password change functionality.
   - Each test class follows the structure of API interaction using the Rest Assured library.

## Rest Assured Library

The **Rest Assured** library is used for testing RESTful APIs in Java. It simplifies the process of making HTTP requests (like GET, POST, PUT, DELETE) and validating responses, making it easier to write automated tests for APIs.

## Dependencies

The project uses **JUnit 5** for running the tests and **Rest Assured** for testing the APIs. The main dependencies are defined in the `pom.xml` file:

```xml
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<rest.assured.version>5.4.0</rest.assured.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>3.8.1</version>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>io.rest-assured</groupId>
			<artifactId>rest-assured</artifactId>
			<version>${rest.assured.version}</version>
		</dependency>

		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.18.32</version>
		</dependency>

		<dependency>
			<groupId>org.apache.logging.log4j</groupId>
			<artifactId>log4j-slf4j-impl</artifactId>
			<version>2.23.1</version>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>8.0.23</version>
		</dependency>

		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.17.1</version>
		</dependency>


	</dependencies>

