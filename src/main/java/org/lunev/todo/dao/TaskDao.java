package org.lunev.todo.dao;

import org.lunev.todo.entity.Status;
import org.lunev.todo.entity.Task;
import org.lunev.todo.entity.User;
import org.lunev.todo.exception.DaoException;
import org.lunev.todo.util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {

    private static final TaskDao INSTANCE = new TaskDao();

    public static final String SAVE_SQL = """
            INSERT INTO todo.tasks(user_id, name, description, category, status, created_at, deadline)
            VALUES (?, ?, ?, ?, ?::todo.task_status, ?, ?);
            """;

    public static final String FIND_ALL_BY_USER_ID = """
            SELECT id, user_id, name, description, category, status, created_at, deadline
            FROM todo.tasks
            WHERE todo.user_id = ?
            """;

    public Task save(Task task){
        try (var connection = ConnectionPool.get();
            var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, task.getUser().getId());
            preparedStatement.setString(2, task.getName());
            if (task.getDescription() == null){
                preparedStatement.setNull(3, Types.LONGVARCHAR);
            } else {
                preparedStatement.setString(3, task.getDescription());
            }
            preparedStatement.setString(4, task.getCategory());
            preparedStatement.setString(5, task.getStatus().name());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(task.getCreatedAt()));
            if (task.getDeadline() == null){
                preparedStatement.setNull(7, Types.TIMESTAMP);
            } else {
                preparedStatement.setTimestamp(7, Timestamp.valueOf(task.getDeadline()));
            }

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                if (generatedKeys.next()){
                    task.setId(generatedKeys.getLong(1));
                }
            }
            return task;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<Task> findAllByUserId(Long userId, Status statusFilter){

        String sql = FIND_ALL_BY_USER_ID;
        if (statusFilter != null){
            sql += " AND status = ?::todo.task_status";
        }

        try (var connection = ConnectionPool.get();
            var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, userId);
            if (statusFilter != null){
                preparedStatement.setString(2, statusFilter.name());
            }
            List<Task> tasks = new ArrayList<>();

            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    tasks.add(buildTask(resultSet));
                }
            }

            return tasks;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Task buildTask(ResultSet resultSet){
        try {
            var task = new Task();
            task.setId(resultSet.getLong("id"));

            User user = new User();
            user.setId(resultSet.getLong("user_id"));
            task.setUser(user);

            task.setName(resultSet.getString("name"));
            task.setDescription(resultSet.getString("description"));
            task.setCategory(resultSet.getString("category"));
            task.setStatus(Status.valueOf(resultSet.getString("status")));
            task.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
            Timestamp deadlineTs = resultSet.getTimestamp("deadline");
            if (deadlineTs != null) {
                task.setDeadline(deadlineTs.toLocalDateTime());
            }

            return task;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public static TaskDao getInstance(){
        return INSTANCE;
    }
}
