package com.janu.taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class TaskRequestDto {

    @NotBlank(message = "Task title is required")
    private String title;

    private String description;
    private String status;
    private String priority;
    private LocalDate dueDate;
    private Long assigneeId;
    private Long creatorId;
    private Long categoryId;

    // Constructors
    public TaskRequestDto() {}

    public TaskRequestDto(String title, String description, String status, String priority, LocalDate dueDate, Long assigneeId, Long creatorId, Long categoryId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.assigneeId = assigneeId;
        this.creatorId = creatorId;
        this.categoryId = categoryId;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Long getAssigneeId() { return assigneeId; }
    public void setAssigneeId(Long assigneeId) { this.assigneeId = assigneeId; }

    public Long getCreatorId() { return creatorId; }
    public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    // Builder
    public static TaskRequestDtoBuilder builder() {
        return new TaskRequestDtoBuilder();
    }

    public static class TaskRequestDtoBuilder {
        private String title;
        private String description;
        private String status;
        private String priority;
        private LocalDate dueDate;
        private Long assigneeId;
        private Long creatorId;
        private Long categoryId;

        public TaskRequestDtoBuilder title(String title) { this.title = title; return this; }
        public TaskRequestDtoBuilder description(String description) { this.description = description; return this; }
        public TaskRequestDtoBuilder status(String status) { this.status = status; return this; }
        public TaskRequestDtoBuilder priority(String priority) { this.priority = priority; return this; }
        public TaskRequestDtoBuilder dueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
        public TaskRequestDtoBuilder assigneeId(Long assigneeId) { this.assigneeId = assigneeId; return this; }
        public TaskRequestDtoBuilder creatorId(Long creatorId) { this.creatorId = creatorId; return this; }
        public TaskRequestDtoBuilder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }

        public TaskRequestDto build() {
            return new TaskRequestDto(title, description, status, priority, dueDate, assigneeId, creatorId, categoryId);
        }
    }
}
