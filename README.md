---

## **Application Documentation**

### **1. Prerequisites**
Before running the application, ensure you have the following installed:
- **Java Development Kit (JDK)**: Version 17 or higher.
- **Maven**: For building and managing dependencies.
- **Git**: For cloning the repository.
- **Postman** or any API testing tool: For interacting with the API endpoints.
- **Database**: Configured in `application.properties` (e.g., H2, MySQL, PostgreSQL).

---

### **2. Cloning the Repository**
Clone the repository to your local machine:
```bash
git clone https://github.com/Elhetamy/maidscc-task.git
cd maidscc-task
```

---

### **3. Building the Application**
Use Maven to build the application:
```bash
mvn clean install
```

---

### **4. Running the Application**
Run the application using Maven:
```bash
mvn spring-boot:run
```
The application will start on port `8080` by default. You can access it at:
```
http://localhost:8080
```

---

### **5. API Endpoints**
Below are the available API endpoints for the application:

#### **Books Management**
- **Get All Books**:
  - **Endpoint**: `GET /books`
  - **Description**: Retrieve a list of all books.
  - **Example Request**:
    ```bash
    curl -X GET http://localhost:8080/books
    ```

- **Get Book by ID**:
  - **Endpoint**: `GET /books/{id}`
  - **Description**: Retrieve a book by its ID.
  - **Example Request**:
    ```bash
    curl -X GET http://localhost:8080/books/1
    ```

- **Add a New Book**:
  - **Endpoint**: `POST /books`
  - **Description**: Add a new book.
  - **Example Request**:
    ```bash
    curl -X POST http://localhost:8080/books \
    -H "Content-Type: application/json" \
    -d '{
          "title": "Sample Book",
          "author": "John Doe",
          "publicationYear": 2023,
          "isbn": "1234567890",
          "genre": "Fiction",
          "availabilityStatus": "Available"
        }'
    ```

- **Update a Book**:
  - **Endpoint**: `PUT /books/{id}`
  - **Description**: Update an existing book by its ID.
  - **Example Request**:
    ```bash
    curl -X PUT http://localhost:8080/books/1 \
    -H "Content-Type: application/json" \
    -d '{
          "title": "Updated Book Title",
          "author": "Jane Doe",
          "publicationYear": 2022,
          "isbn": "0987654321",
          "genre": "Non-Fiction",
          "availabilityStatus": "Borrowed"
        }'
    ```

- **Delete a Book**:
  - **Endpoint**: `DELETE /books/{id}`
  - **Description**: Delete a book by its ID.
  - **Example Request**:
    ```bash
    curl -X DELETE http://localhost:8080/books/1
    ```

---

#### **Patrons Management**
- **Get All Patrons**:
  - **Endpoint**: `GET /patrons`
  - **Description**: Retrieve a list of all patrons.
  - **Example Request**:
    ```bash
    curl -X GET http://localhost:8080/patrons
    ```

- **Get Patron by ID**:
  - **Endpoint**: `GET /patrons/{id}`
  - **Description**: Retrieve a patron by their ID.
  - **Example Request**:
    ```bash
    curl -X GET http://localhost:8080/patrons/1
    ```

- **Add a New Patron**:
  - **Endpoint**: `POST /patrons`
  - **Description**: Add a new patron.
  - **Example Request**:
    ```bash
    curl -X POST http://localhost:8080/patrons \
    -H "Content-Type: application/json" \
    -d '{
          "name": "John Doe",
          "email": "john.doe@example.com",
          "phoneNumber": "1234567890",
          "address": "123 Main St",
          "membershipStatus": "Active"
        }'
    ```

- **Update a Patron**:
  - **Endpoint**: `PUT /patrons/{id}`
  - **Description**: Update an existing patron by their ID.
  - **Example Request**:
    ```bash
    curl -X PUT http://localhost:8080/patrons/1 \
    -H "Content-Type: application/json" \
    -d '{
          "name": "Jane Doe",
          "email": "jane.doe@example.com",
          "phoneNumber": "0987654321",
          "address": "456 Elm St",
          "membershipStatus": "Inactive"
        }'
    ```

- **Delete a Patron**:
  - **Endpoint**: `DELETE /patrons/{id}`
  - **Description**: Delete a patron by their ID.
  - **Example Request**:
    ```bash
    curl -X DELETE http://localhost:8080/patrons/1
    ```

---

#### **Borrowing Management**
- **Borrow a Book**:
  - **Endpoint**: `POST /borrow/{bookId}/patron/{patronId}`
  - **Description**: Borrow a book by a patron.
  - **Example Request**:
    ```bash
    curl -X POST http://localhost:8080/borrow/1/patron/1
    ```

- **Return a Book**:
  - **Endpoint**: `PUT /return/{bookId}/patron/{patronId}`
  - **Description**: Return a borrowed book by a patron.
  - **Example Request**:
    ```bash
    curl -X PUT http://localhost:8080/return/1/patron/1
    ```

---

### **6. Authentication**

1. **Obtain a JWT Token**:
   - **Endpoint**: `POST /auth/login`
   - **Example Request**:
     ```bash
     curl -X POST http://localhost:8080/authenticate \
     -H "Content-Type: application/json" \
     -d '{
           "username": "admin",
           "password": "password"
         }'
     ```

2. **Use the Token in API Requests**:
   Include the JWT token in the `Authorization` header for authenticated endpoints:
   ```bash
   curl -X GET http://localhost:8080/books \
   -H "Authorization: Bearer <your-jwt-token>"
   ```

---

### **7. Database Configuration**
The application uses an in-memory H2 database by default. You can access the H2 console at:
```
http://localhost:8080/h2-console
```
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (leave empty)

To use a different database (e.g., MySQL, PostgreSQL), update the `application.properties` file:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/maidscc_task
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

---
