package com.janu.taskmanagement.service;

import java.util.concurrent.CompletableFuture;

public interface TaskReportService {
    CompletableFuture<String> generateTaskReportAsync(Long userId);
}
