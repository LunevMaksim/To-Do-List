package org.lunev.todo.dao;

import org.lunev.todo.entity.Task;
import org.lunev.todo.exception.DaoException;
import org.lunev.todo.util.ConnectionPool;

import java.sql.*;

public class TaskDao {

    public static final String SAVE_SQL = """
            INSERT INTO todo.tasks(user_id, name, description, category, status, created_at, deadline)
            VALUES (?, ?, ?, ?, ?::todo.task_status, ?, ?);
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
}
