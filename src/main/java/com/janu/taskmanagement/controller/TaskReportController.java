package com.janu.taskmanagement.controller;

import com.janu.taskmanagement.service.TaskReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/tasks/reports")
public class TaskReportController {

    private final TaskReportService taskReportService;

    public TaskReportController(TaskReportService taskReportService) {
        this.taskReportService = taskReportService;
    }

    /**
     * Non-blocking REST Endpoint. 
     * Spring frees up the servlet thread immediately. The client response is sent
     * once the background CompletableFuture completes.
     */
    @PostMapping("/generate")
    public CompletableFuture<ResponseEntity<String>> generateReport(@RequestParam Long userId) {
        return taskReportService.generateTaskReportAsync(userId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to generate report: " + ex.getMessage()));
    }
}
