package com.janu.taskmanagement.service;

import com.janu.taskmanagement.dto.TaskRequestDto;
import com.janu.taskmanagement.dto.TaskResponseDto;

import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(TaskRequestDto taskRequestDto);
    TaskResponseDto getTaskById(Long id);
    List<TaskResponseDto> getAllTasks(String status, String priority, Long categoryId, String sortBy);
    TaskResponseDto updateTask(Long id, TaskRequestDto taskRequestDto);
    TaskResponseDto updateTaskStatus(Long id, String status);
    void deleteTask(Long id);
    List<TaskResponseDto> getTasksByAssignee(Long assigneeId);
}
