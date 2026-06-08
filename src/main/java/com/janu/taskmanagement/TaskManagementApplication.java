package com.janu.taskmanagement;

import com.janu.taskmanagement.entity.*;
import com.janu.taskmanagement.repository.CategoryRepository;
import com.janu.taskmanagement.repository.TaskRepository;
import com.janu.taskmanagement.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDate;

@SpringBootApplication
@EnableAsync // Required to activate asynchronous thread execution using @Async
public class TaskManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagementApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            TaskRepository taskRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                // Seed Users
                User dev = User.builder()
                        .username("janakiraman")
                        .password("devpass123")
                        .email("janakiraman@company.com")
                        .role("ROLE_ADMIN")
                        .build();
                User u1 = User.builder()
                        .username("alice")
                        .password("alicepass")
                        .email("alice@company.com")
                        .role("ROLE_USER")
                        .build();
                User u2 = User.builder()
                        .username("bob")
                        .password("bobpass")
                        .email("bob@company.com")
                        .role("ROLE_USER")
                        .build();

                dev = userRepository.save(dev);
                u1 = userRepository.save(u1);
                u2 = userRepository.save(u2);

                // Seed Categories
                Category feature = Category.builder()
                        .name("Feature Development")
                        .description("Creation of new endpoints, tables, and services.")
                        .build();
                Category bug = Category.builder()
                        .name("Bug Fixing")
                        .description("Resolving errors and edge cases in the code.")
                        .build();
                Category docs = Category.builder()
                        .name("Documentation")
                        .description("Writing READMEs, wikis, and technical notes.")
                        .build();

                feature = categoryRepository.save(feature);
                bug = categoryRepository.save(bug);
                docs = categoryRepository.save(docs);

                // Seed Tasks
                Task t1 = Task.builder()
                        .title("Develop Student Management REST API")
                        .description("Build out 15+ CRUD endpoints for student, department, course, and enrollment.")
                        .status(TaskStatus.COMPLETED)
                        .priority(TaskPriority.HIGH)
                        .dueDate(LocalDate.now().minusDays(1))
                        .creator(dev)
                        .assignee(dev)
                        .category(feature)
                        .build();

                Task t2 = Task.builder()
                        .title("Configure MySQL Profiles")
                        .description("Add database configurations in properties files for local H2 and remote MySQL deployment.")
                        .status(TaskStatus.IN_PROGRESS)
                        .priority(TaskPriority.MEDIUM)
                        .dueDate(LocalDate.now().plusDays(2))
                        .creator(dev)
                        .assignee(u1)
                        .category(feature)
                        .build();

                Task t3 = Task.builder()
                        .title("Write postman collection documentation")
                        .description("Provide Curl statements for local execution testing.")
                        .status(TaskStatus.TODO)
                        .priority(TaskPriority.LOW)
                        .dueDate(LocalDate.now().plusDays(5))
                        .creator(dev)
                        .assignee(u2)
                        .category(docs)
                        .build();

                Task t4 = Task.builder()
                        .title("Fix validation exceptions mapping")
                        .description("Ensure MethodArgumentNotValidException is handled gracefully in GlobalExceptionHandler.")
                        .status(TaskStatus.COMPLETED)
                        .priority(TaskPriority.HIGH)
                        .dueDate(LocalDate.now().minusDays(2))
                        .creator(dev)
                        .assignee(dev)
                        .category(bug)
                        .build();

                taskRepository.save(t1);
                taskRepository.save(t2);
                taskRepository.save(t3);
                taskRepository.save(t4);

                System.out.println(">>> Mock database initialization complete: seeded users, categories, and tasks.");
            }
        };
    }
}
