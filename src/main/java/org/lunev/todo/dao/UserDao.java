package org.lunev.todo.dao;

import org.lunev.todo.entity.User;
import org.lunev.todo.exception.DaoException;
import org.lunev.todo.util.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UserDao {

    public static final String SAVE_SQL = """
            INSERT INTO todo.users(surname, name, patronymic)
            VALUES (?, ?, ?);
            """;

    public static final String FIND_BY_ID = """
            SELECT id, surname, name, patronymic
            FROM todo.users
            WHERE id = ?;
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

    public Optional<User> findById(Long id){
        try (var connection = ConnectionPool.get();
            var preparedStatement = connection.prepareStatement(FIND_BY_ID)) {

            preparedStatement.setLong(1, id);
            try (var resultSet = preparedStatement.executeQuery()) {
                User user = null;
                if (resultSet.next()) {
                    user = new User(resultSet.getLong(1), resultSet.getString(2),
                            resultSet.getString(3), resultSet.getString(4));
                }
                return Optional.ofNullable(user);
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
