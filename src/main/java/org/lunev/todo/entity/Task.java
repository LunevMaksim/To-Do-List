package org.lunev.todo.entity;

import java.time.LocalDateTime;

public class Task {
    private Long id;
    private User userId;
    private String name;
    private String description;
    private String category;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;

    public Task(){}

    public Task(Long id, User userId, String name, String description,
                String category, Status status, LocalDateTime createdAt, LocalDateTime deadline) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.status = status;
        this.createdAt = createdAt;
        this.deadline = deadline;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getUserId() {
        return userId;
    }
    public void setUserId(User userId) {
        this.userId = userId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getDeadline() {
        return deadline;
    }
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
