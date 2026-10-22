package org.lunev.todo.service;

import org.lunev.todo.dao.TaskDao;
import org.lunev.todo.dao.UserDao;
import org.lunev.todo.dto.TaskCreateDto;
import org.lunev.todo.entity.Status;
import org.lunev.todo.entity.Task;
import org.lunev.todo.entity.User;
import org.lunev.todo.exception.DaoException;
import org.lunev.todo.util.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TaskService {
    private static final TaskService INSTANCE = new TaskService();

    private TaskService() {}

    public Long createTask(TaskCreateDto dto){
        try (Connection connection = ConnectionPool.get()){
            return createTask(connection, dto);
        } catch (SQLException e){
            throw new DaoException(e);
        }
    }

    public Long createTask(Connection connection, TaskCreateDto dto){
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

        TaskDao.getInstance().save(connection, task);

        return task.getId();
    }

    public void registerUserWithWelcomeTask(User user, Task task){
        try (Connection connection = ConnectionPool.get()){
            connection.setAutoCommit(false);

            try {
                UserDao.getInstance().save(connection, user);
                TaskDao.getInstance().save(connection, task);

                connection.commit();
            } catch (SQLException e){
                try {
                    connection.rollback();
                    throw e;
                } catch (SQLException exception){
                    throw new RuntimeException(exception);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static TaskService getInstance(){
        return INSTANCE;
    }
}
