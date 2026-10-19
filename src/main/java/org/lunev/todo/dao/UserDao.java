package org.lunev.todo.dao;

import org.lunev.todo.entity.User;
import org.lunev.todo.exception.DaoException;
import org.lunev.todo.util.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDao {

    public static final String SAVE_SQL = """
            INSERT INTO todo.users(surname, name, patronymic)
            VALUES (?, ?, ?);
            """;

    public User save(User user){
        try (var connection = ConnectionPool.get();
            var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getSurname());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getPatronymic());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                if (generatedKeys.next()){
                    user.setId(generatedKeys.getLong(1));
                }
            }
            return user;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
