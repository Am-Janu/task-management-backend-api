# task-management-backend-api
Spring Boot task management API implementing CRUD operations, Java 8 Streams, Lambda Expressions, Multithreading, and MySQL.

# Task Management Backend API

A Spring Boot and Maven-based RESTful API designed to manage and organize Tasks, Users, and Categories.

This project implements a clean **Controller-Service-Repository** pattern and highlights advanced concepts like **Java 8 Streams & Lambdas** and **Multithreading/Asynchronous execution**.

## Tech Stack
- **Framework**: Spring Boot 3.2.5
- **Build Tool**: Maven
- **Database**: H2 (In-Memory for Dev) / MySQL (Production Profile)
- **ORM**: Hibernate / Spring Data JPA
- **Language**: Java 17

---

## Special Features (Resume Alignments)

### 1. Java 8 Streams, Lambdas & Custom Comparators
In `TaskServiceImpl.java`, we utilize Java 8 Streams and Lambdas to perform in-memory filtering and sorting on tasks:
- **Filtering**: Tasks can be filtered by `status`, `priority`, and `categoryId` dynamically using `stream().filter()`.
- **Custom Sorting**: Sorting is applied via `stream().sorted(Comparator<Task>)` using custom comparators (e.g., sorting tasks by priority high-to-low, or null-safe due-date sorting).
- **DTO Mapping**: Entities are mapped to response DTOs using `.map(this::mapToDto)` in a clean, declarative pipeline.

### 2. Multithreading & Asynchronous Report Generation
In `TaskReportServiceImpl.java` and `TaskReportController.java`, we demonstrate asynchronous processing:
- **`@Async` Thread Pooling**: The CPU-intensive stats compilation (calculating metrics, overdue counts) is executed on a background thread.
- **`CompletableFuture`**: The REST controller triggers the process asynchronously, returning a non-blocking `CompletableFuture<ResponseEntity<String>>`. This frees up the servlet thread immediately to handle other incoming requests, while the client receives the report payload once the background thread finishes its work.

---

## Getting Started

### Prerequisites
- JDK 17 or higher
- Maven 3.6+

### Build the Project
```bash
mvn clean package
```

### Run the Application
```bash
mvn spring-boot:run
```
Once started, the server runs locally on: **`http://localhost:8081`**

### Accessing the Database (H2 Console)
1. Visit: `http://localhost:8081/h2-console`
2. **JDBC URL**: `jdbc:h2:mem:taskdb`
3. **Username**: `sa`
4. **Password**: *(Leave blank)*

---

## API Endpoints

### 1. Users
- **Register User**: `POST /api/users`
- **Get All Users**: `GET /api/users`
- **Get User by ID**: `GET /api/users/{id}`

### 2. Categories
- **Create Category**: `POST /api/categories`
- **Get All Categories**: `GET /api/categories`
- **Get Category by ID**: `GET /api/categories/{id}`
- **Delete Category**: `DELETE /api/categories/{id}`

### 3. Tasks
- **Create Task**: `POST /api/tasks`
- **Get All Tasks**: `GET /api/tasks` (Supports filtering & sorting)
  - *Query Params*: `status`, `priority`, `categoryId`, `sortBy` (Options: `due_date`, `priority`, `title`, `created_at`)
- **Get Task by ID**: `GET /api/tasks/{id}`
- **Update Task**: `PUT /api/tasks/{id}`
- **Update Task Status**: `PATCH /api/tasks/{id}/status?status={status}`
- **Delete Task**: `DELETE /api/tasks/{id}`
- **Get Tasks by Assignee**: `GET /api/tasks/assignee/{assigneeId}`

### 4. Async Reports
- **Generate Report Async**: `POST /api/tasks/reports/generate?userId={userId}` (Executes on a background thread and returns the report once completed)

---

## Example Curl Commands

### 1. Retrieve Tasks Filtered by status and Sorted by Priority
```bash
curl -X GET "http://localhost:8081/api/tasks?status=COMPLETED&sortBy=priority"
```

### 2. Register a New User
```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "developer_janu",
    "password": "securepassword123",
    "email": "janu.dev@gmail.com"
  }'
```

### 3. Trigger Asynchronous Task Performance Report
This simulates a heavy background job (with a 3-second delay) executing on a separate worker thread:
```bash
curl -X POST "http://localhost:8081/api/tasks/reports/generate?userId=1"
```
**Example Console Log during execution**:
```text
[AsyncExecutor-1] com.janu.taskmanagement.service.impl.TaskReportServiceImpl : Starting asynchronous report generation for user ID: 1 on thread: AsyncExecutor-1
[AsyncExecutor-1] com.janu.taskmanagement.service.impl.TaskReportServiceImpl : Asynchronous report generation completed for user ID: 1 on thread: AsyncExecutor-1
```
