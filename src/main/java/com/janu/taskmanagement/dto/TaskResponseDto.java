package com.janu.taskmanagement.dto;

import com.janu.taskmanagement.entity.TaskPriority;
import com.janu.taskmanagement.entity.TaskStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskResponseDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDto assignee;
    private UserDto creator;
    private CategoryDto category;

    // Constructors
    public TaskResponseDto() {}

    public TaskResponseDto(Long id, String title, String description, TaskStatus status, TaskPriority priority, LocalDate dueDate, LocalDateTime createdAt, LocalDateTime updatedAt, UserDto assignee, UserDto creator, CategoryDto category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.assignee = assignee;
        this.creator = creator;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public TaskPriority getPriority() { return priority; }
    public void setPriority(TaskPriority priority) { this.priority = priority; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public UserDto getAssignee() { return assignee; }
    public void setAssignee(UserDto assignee) { this.assignee = assignee; }

    public UserDto getCreator() { return creator; }
    public void setCreator(UserDto creator) { this.creator = creator; }

    public CategoryDto getCategory() { return category; }
    public void setCategory(CategoryDto category) { this.category = category; }

    // Builder
    public static TaskResponseDtoBuilder builder() {
        return new TaskResponseDtoBuilder();
    }

    public static class TaskResponseDtoBuilder {
        private Long id;
        private String title;
        private String description;
        private TaskStatus status;
        private TaskPriority priority;
        private LocalDate dueDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private UserDto assignee;
        private UserDto creator;
        private CategoryDto category;

        public TaskResponseDtoBuilder id(Long id) { this.id = id; return this; }
        public TaskResponseDtoBuilder title(String title) { this.title = title; return this; }
        public TaskResponseDtoBuilder description(String description) { this.description = description; return this; }
        public TaskResponseDtoBuilder status(TaskStatus status) { this.status = status; return this; }
        public TaskResponseDtoBuilder priority(TaskPriority priority) { this.priority = priority; return this; }
        public TaskResponseDtoBuilder dueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
        public TaskResponseDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public TaskResponseDtoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public TaskResponseDtoBuilder assignee(UserDto assignee) { this.assignee = assignee; return this; }
        public TaskResponseDtoBuilder creator(UserDto creator) { this.creator = creator; return this; }
        public TaskResponseDtoBuilder category(CategoryDto category) { this.category = category; return this; }

        public TaskResponseDto build() {
            return new TaskResponseDto(id, title, description, status, priority, dueDate, createdAt, updatedAt, assignee, creator, category);
        }
    }
}
