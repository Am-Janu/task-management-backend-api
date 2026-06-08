package com.janu.taskmanagement.service.impl;

import com.janu.taskmanagement.entity.Task;
import com.janu.taskmanagement.entity.TaskStatus;
import com.janu.taskmanagement.entity.User;
import com.janu.taskmanagement.exception.ResourceNotFoundException;
import com.janu.taskmanagement.repository.TaskRepository;
import com.janu.taskmanagement.repository.UserRepository;
import com.janu.taskmanagement.service.TaskReportService;
import com.janu.taskmanagement.repository.TaskRepository;
import com.janu.taskmanagement.repository.UserRepository;
import com.janu.taskmanagement.service.TaskReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskReportServiceImpl implements TaskReportService {

    private static final Logger log = LoggerFactory.getLogger(TaskReportServiceImpl.class);

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskReportServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Async // Runs asynchronously in a separate thread from Spring's TaskExecutor
    @Transactional(readOnly = true)
    public CompletableFuture<String> generateTaskReportAsync(Long userId) {
        log.info("Starting asynchronous report generation for user ID: {} on thread: {}", 
                userId, Thread.currentThread().getName());

        // Simulate heavy processing (e.g. compiling data, generating PDFs, sending mock emails)
        try {
            Thread.sleep(3000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Report thread interrupted for user ID: {}", userId);
            return CompletableFuture.failedFuture(new RuntimeException("Report processing interrupted"));
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        List<Task> userTasks = taskRepository.findByAssigneeId(userId);

        // Compute report stats using Java 8 Streams
        long totalTasks = userTasks.size();
        long completedTasks = userTasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.COMPLETED)
                .count();
        long pendingTasks = userTasks.stream()
                .filter(t -> t.getStatus() == TaskStatus.TODO || t.getStatus() == TaskStatus.IN_PROGRESS)
                .count();
        long overdueTasks = userTasks.stream()
                .filter(t -> t.getStatus() != TaskStatus.COMPLETED && 
                            t.getDueDate() != null && 
                            t.getDueDate().isBefore(LocalDate.now()))
                .count();

        String reportSummary = String.format(
            "========================================\n" +
            "TASK MANAGEMENT PERFORMANCE REPORT\n" +
            "========================================\n" +
            "User: %s (%s)\n" +
            "Generated At: %s\n" +
            "Running on Thread: %s\n" +
            "----------------------------------------\n" +
            "Total Tasks Assigned: %d\n" +
            "Completed Tasks:      %d (%.1f%%)\n" +
            "Pending Tasks:        %d\n" +
            "Overdue Tasks:        %d\n" +
            "========================================",
            user.getUsername(),
            user.getEmail(),
            LocalDateTime.now(),
            Thread.currentThread().getName(),
            totalTasks,
            completedTasks,
            totalTasks > 0 ? ((double) completedTasks / totalTasks) * 100 : 0.0,
            pendingTasks,
            overdueTasks
        );

        log.info("Asynchronous report generation completed for user ID: {} on thread: {}", 
                userId, Thread.currentThread().getName());
        return CompletableFuture.completedFuture(reportSummary);
    }
}
