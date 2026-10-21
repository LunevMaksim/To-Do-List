package org.lunev.todo.service;

import org.lunev.todo.dao.TaskDao;
import org.lunev.todo.dto.TaskCreateDto;
import org.lunev.todo.entity.Status;
import org.lunev.todo.entity.Task;
import org.lunev.todo.entity.User;

import java.time.LocalDateTime;

public class TaskService {
    private static final TaskService INSTANCE = new TaskService();

    private TaskService() {}

    public Long createTask(TaskCreateDto dto){
        Long createId = null;

        if (dto.userId() == null || dto.name().isBlank()){
            throw new IllegalArgumentException();
        }

        Task task = new Task();

        User user = new User();
        user.setId(dto.userId());

        task.setUser(user);
        task.setName(dto.name());
        task.setDescription(dto.description());
        task.setCategory(dto.category());
        task.setStatus(Status.NEW);
        task.setCreatedAt(LocalDateTime.now());
        task.setDeadline(dto.deadline());

        TaskDao.getInstance().save(task);

        return task.getId();
    }

    public static TaskService getInstance(){
        return INSTANCE;
    }
}
