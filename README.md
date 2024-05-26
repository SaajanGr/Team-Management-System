# Team Management System (TMS)

## Description

The Team Management System (TMS) is a Spring Boot application designed to manage team members efficiently. The system provides functionalities to create, read, update, and delete team members' information through a RESTful API. The application is structured with separate layers for controllers, services, and repositories, ensuring a clean and maintainable codebase. Additionally, TMS includes comprehensive unit and integration tests using JUnit and Mockito to ensure the reliability and correctness of the application's functionality.

## Features

- **CRUD Operations:** Manage team members' information through RESTful API endpoints.
- **Service Layer:** Contains business logic for managing team members.
- **Repository Layer:** Interfaces with the database to perform CRUD operations.
- **Controller Layer:** Handles HTTP requests and maps them to the service layer.
- **Unit and Integration Testing:** Comprehensive tests using JUnit and Mockito to ensure the functionality of controllers, services, and repositories.

## Using Postman

To test the Team Management System API using Postman, follow these steps:

1. **Install Postman:** Download and install Postman from the [official website](https://www.postman.com/downloads/).
2. **Import Requests:** Create a new collection in Postman and add requests for the following endpoints:

    - **Create a new team member:**
      - Method: POST
      - URL: `http://localhost:8080/api/team`
      - Body (JSON):
        ```json
        {
          "memberId": "TM456",
          "firstName": "John",
          "lastName": "Doe",
          "email": "jd@domain.com"
        }
        ```

    - **Retrieve all team members:**
      - Method: GET
      - URL: `http://localhost:8080/api/team`

    - **Retrieve a team member by ID:**
      - Method: GET
      - URL: `http://localhost:8080/api/team/TM123`

    - **Update a team member:**
      - Method: PUT
      - URL: `http://localhost:8080/api/team/TM123`
      - Body (JSON):
        ```json
        {
          "firstName": "Jack",
          "lastName": "Green",
          "email": "jg@domain.com"
        }
        ```

    - **Delete a team member:**
      - Method: DELETE
      - URL: `http://localhost:8080/api/team/TM123`

3. **Environment Variables (Optional):** Create an environment in Postman with the following variable:
    - Variable: `baseUrl`
    - Initial Value: `http://localhost:8080`

4. **Using Variables in Requests:** Replace the base URL in your requests with `{{baseUrl}}`, e.g., `{{baseUrl}}/api/team`.

## Installation and Usage

### Prerequisites

- Java 11 or higher
- Maven
- MySQL

### Setup

1. **Clone the repository:**
    ```sh
    git clone https://github.com/yourusername/TMS.git
    cd TMS
    ```

2. **Configure MySQL:**
    - Create a database named `team_management`.
    - Update the `application.properties` file with your MySQL database credentials:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/team_management
    spring.datasource.username=yourusername
    spring.datasource.password=yourpassword
    spring.jpa.hibernate.ddl-auto=update
    ```

3. **Build the project:**
    ```sh
    mvn clean install
    ```

4. **Run the application:**
    ```sh
    mvn spring-boot:run
    ```

### Running the Application

- Ensure that MySQL is running on your local machine or your specified server.
- Run the backend setup as described above.
- Open your browser and go to `http://localhost:8080` to access the API.

## Technologies Used

### Backend:

- Java
- Spring Boot
- MySQL
- JPA (Java Persistence API)
- Hibernate
- Maven
- JUnit (for testing)
- Mockito (for mocking in tests)

## Future Improvements

- **Add Authentication and Authorization:** Secure the API endpoints using Spring Security.
- **Implement Pagination and Sorting:** Add pagination and sorting capabilities for retrieving team members.
- **Enhance Error Handling and Validation:** Improve error handling and add more detailed validation for user inputs.
- **Create a Frontend Interface:** Develop a frontend interface using React or Angular to interact with the API.

