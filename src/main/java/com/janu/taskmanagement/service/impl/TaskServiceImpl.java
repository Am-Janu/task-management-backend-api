package com.janu.taskmanagement.service.impl;

import com.janu.taskmanagement.dto.CategoryDto;
import com.janu.taskmanagement.dto.TaskRequestDto;
import com.janu.taskmanagement.dto.TaskResponseDto;
import com.janu.taskmanagement.dto.UserDto;
import com.janu.taskmanagement.entity.*;
import com.janu.taskmanagement.exception.BadRequestException;
import com.janu.taskmanagement.exception.ResourceNotFoundException;
import com.janu.taskmanagement.repository.CategoryRepository;
import com.janu.taskmanagement.repository.TaskRepository;
import com.janu.taskmanagement.repository.UserRepository;
import com.janu.taskmanagement.service.TaskService;
import com.janu.taskmanagement.repository.CategoryRepository;
import com.janu.taskmanagement.repository.TaskRepository;
import com.janu.taskmanagement.repository.UserRepository;
import com.janu.taskmanagement.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public TaskResponseDto createTask(TaskRequestDto dto) {
        User creator = userRepository.findById(dto.getCreatorId())
                .orElseThrow(() -> new ResourceNotFoundException("Creator user not found with ID: " + dto.getCreatorId()));

        User assignee = null;
        if (dto.getAssigneeId() != null) {
            assignee = userRepository.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee user not found with ID: " + dto.getAssigneeId()));
        }

        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + dto.getCategoryId()));
        }

        TaskStatus status = TaskStatus.TODO;
        if (dto.getStatus() != null) {
            try {
                status = TaskStatus.valueOf(dto.getStatus().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("Invalid task status: " + dto.getStatus());
            }
        }

        TaskPriority priority = TaskPriority.MEDIUM;
        if (dto.getPriority() != null) {
            try {
                priority = TaskPriority.valueOf(dto.getPriority().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("Invalid task priority: " + dto.getPriority());
            }
        }

        Task task = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(status)
                .priority(priority)
                .dueDate(dto.getDueDate())
                .creator(creator)
                .assignee(assignee)
                .category(category)
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToDto(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));
        return mapToDto(task);
    }

    /**
     * Demonstrating Java 8 Streams, Lambdas, and Comparators for in-memory filtering and sorting.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDto> getAllTasks(String status, String priority, Long categoryId, String sortBy) {
        List<Task> allTasks = taskRepository.findAll();
        Stream<Task> taskStream = allTasks.stream();

        // 1. Filter by Status (Lambda & Stream Filter)
        if (status != null && !status.trim().isEmpty()) {
            try {
                TaskStatus targetStatus = TaskStatus.valueOf(status.toUpperCase());
                taskStream = taskStream.filter(task -> task.getStatus() == targetStatus);
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("Invalid status filter: " + status);
            }
        }

        // 2. Filter by Priority (Lambda & Stream Filter)
        if (priority != null && !priority.trim().isEmpty()) {
            try {
                TaskPriority targetPriority = TaskPriority.valueOf(priority.toUpperCase());
                taskStream = taskStream.filter(task -> task.getPriority() == targetPriority);
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("Invalid priority filter: " + priority);
            }
        }

        // 3. Filter by Category (Lambda & Stream Filter)
        if (categoryId != null) {
            taskStream = taskStream.filter(task -> task.getCategory() != null && 
                    Objects.equals(task.getCategory().getId(), categoryId));
        }

        // 4. Sort using Custom Comparator comparisons
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Comparator<Task> taskComparator;
            switch (sortBy.toLowerCase()) {
                case "due_date":
                case "duedate":
                    taskComparator = Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()));
                    break;
                case "priority":
                    // High -> Medium -> Low custom enum ranking
                    taskComparator = Comparator.comparing(Task::getPriority, (p1, p2) -> p2.compareTo(p1));
                    break;
                case "title":
                    taskComparator = Comparator.comparing(Task::getTitle, String.CASE_INSENSITIVE_ORDER);
                    break;
                case "created_at":
                case "createdat":
                default:
                    taskComparator = Comparator.comparing(Task::getCreatedAt);
                    break;
            }
            taskStream = taskStream.sorted(taskComparator);
        }

        // 5. Transform Entity to DTO using Stream Map and collect to List
        return taskStream
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(Long id, TaskRequestDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));

        User assignee = null;
        if (dto.getAssigneeId() != null) {
            assignee = userRepository.findById(dto.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee user not found with ID: " + dto.getAssigneeId()));
        }

        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + dto.getCategoryId()));
        }

        if (dto.getStatus() != null) {
            try {
                task.setStatus(TaskStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("Invalid task status: " + dto.getStatus());
            }
        }

        if (dto.getPriority() != null) {
            try {
                task.setPriority(TaskPriority.valueOf(dto.getPriority().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("Invalid task priority: " + dto.getPriority());
            }
        }

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setAssignee(assignee);
        task.setCategory(category);

        Task updatedTask = taskRepository.save(task);
        return mapToDto(updatedTask);
    }

    @Override
    @Transactional
    public TaskResponseDto updateTaskStatus(Long id, String statusStr) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));

        try {
            TaskStatus status = TaskStatus.valueOf(statusStr.toUpperCase());
            task.setStatus(status);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid task status: " + statusStr);
        }

        Task updatedTask = taskRepository.save(task);
        return mapToDto(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));
        taskRepository.delete(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDto> getTasksByAssignee(Long assigneeId) {
        if (!userRepository.existsById(assigneeId)) {
            throw new ResourceNotFoundException("User not found with ID: " + assigneeId);
        }
        List<Task> tasks = taskRepository.findByAssigneeId(assigneeId);
        return tasks.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // Mapper Methods
    private TaskResponseDto mapToDto(Task entity) {
        UserDto creatorDto = UserDto.builder()
                .id(entity.getCreator().getId())
                .username(entity.getCreator().getUsername())
                .email(entity.getCreator().getEmail())
                .role(entity.getCreator().getRole())
                .build();

        UserDto assigneeDto = null;
        if (entity.getAssignee() != null) {
            assigneeDto = UserDto.builder()
                    .id(entity.getAssignee().getId())
                    .username(entity.getAssignee().getUsername())
                    .email(entity.getAssignee().getEmail())
                    .role(entity.getAssignee().getRole())
                    .build();
        }

        CategoryDto categoryDto = null;
        if (entity.getCategory() != null) {
            categoryDto = CategoryDto.builder()
                    .id(entity.getCategory().getId())
                    .name(entity.getCategory().getName())
                    .description(entity.getCategory().getDescription())
                    .build();
        }

        return TaskResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .priority(entity.getPriority())
                .dueDate(entity.getDueDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .creator(creatorDto)
                .assignee(assigneeDto)
                .category(categoryDto)
                .build();
    }
}
