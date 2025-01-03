package lt.ehu.student.moneytrackerbeta.dao.impl;

import lt.ehu.student.moneytrackerbeta.connection.ConnectionPool;
import lt.ehu.student.moneytrackerbeta.constant.DatabaseColumnName;
import lt.ehu.student.moneytrackerbeta.dao.BaseDao;
import lt.ehu.student.moneytrackerbeta.exception.ConnectionPoolException;
import lt.ehu.student.moneytrackerbeta.exception.DaoException;
import lt.ehu.student.moneytrackerbeta.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDao implements BaseDao<User> {
    private static final Logger logger = LogManager.getLogger(UserDao.class.getName());

    private static final String SELECT_ALL_USERS = "SELECT " + DatabaseColumnName.USER_ID + ", " + DatabaseColumnName.USER_LOGIN + ", " + DatabaseColumnName.USER_PASSWORD_HASH + ", " + DatabaseColumnName.USER_FIRST_NAME + ", " + DatabaseColumnName.USER_LAST_NAME + ", " + DatabaseColumnName.USER_DEFAULT_CURRENCY + ", " + DatabaseColumnName.USER_EMAIL + ", " + DatabaseColumnName.USER_REGISTRATION_DATE + " FROM public.user";
    private static final String SELECT_USER_BY_LOGIN = "SELECT " + DatabaseColumnName.USER_ID + ", " + DatabaseColumnName.USER_LOGIN + ", " + DatabaseColumnName.USER_PASSWORD_HASH + ", " + DatabaseColumnName.USER_FIRST_NAME + ", " + DatabaseColumnName.USER_LAST_NAME + ", " + DatabaseColumnName.USER_DEFAULT_CURRENCY + ", " + DatabaseColumnName.USER_EMAIL + ", " + DatabaseColumnName.USER_REGISTRATION_DATE + " FROM public.user WHERE " + DatabaseColumnName.USER_LOGIN + " = ?";
    private static final String INSERT_USER = "INSERT INTO public.user (" + DatabaseColumnName.USER_LOGIN + ", " + DatabaseColumnName.USER_PASSWORD_HASH + ", " + DatabaseColumnName.USER_FIRST_NAME + ", " + DatabaseColumnName.USER_LAST_NAME + ", " + DatabaseColumnName.USER_DEFAULT_CURRENCY + ", " + DatabaseColumnName.USER_EMAIL + ", " + DatabaseColumnName.USER_REGISTRATION_DATE + ") VALUES (?, ?, ?, ?, ?, ?, ?)";

    public List<User> findAll() throws DaoException {
        List<User> users = new ArrayList<>();
        PreparedStatement statement;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SELECT_ALL_USERS);
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS);
            while (resultSet.next()) {
                int id = resultSet.getInt(DatabaseColumnName.USER_ID);
                String login = resultSet.getString(DatabaseColumnName.USER_LOGIN);
                String passwordHash = resultSet.getString(DatabaseColumnName.USER_PASSWORD_HASH);
                String firstName = resultSet.getString(DatabaseColumnName.USER_FIRST_NAME);
                String lastName = resultSet.getString(DatabaseColumnName.USER_LAST_NAME);
                int defaultCurrency = Integer.parseInt(resultSet.getString(DatabaseColumnName.USER_DEFAULT_CURRENCY));
                String email = resultSet.getString(DatabaseColumnName.USER_EMAIL);
                Timestamp registrationDate = resultSet.getTimestamp(DatabaseColumnName.USER_REGISTRATION_DATE);
                // Create User objects and add them to the list
                User user = new User(id, login, passwordHash, firstName, lastName, defaultCurrency, email, registrationDate);
                users.add(user);
            }
        } catch (SQLException e) {
            logger.error("Error while retrieving users from the database", e);
            throw new DaoException("Error while retrieving users from the database", e);
        } catch (ConnectionPoolException e) {
            logger.fatal("Error while retrieving users from the database", e);
            throw new RuntimeException("Error while retrieving users from the database", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
        return users;
    }

    @Override
    public User findById(int id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Finding user by ID is not supported yet.");
    }

    @Override
    public User findById(UUID id) throws DaoException {
        return null;
    }

    public User findByLogin(String login) throws DaoException {
        Connection connection = null;
        PreparedStatement statement;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SELECT_USER_BY_LOGIN);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt(DatabaseColumnName.USER_ID);
                String passwordHash = resultSet.getString(DatabaseColumnName.USER_PASSWORD_HASH);
                String firstName = resultSet.getString(DatabaseColumnName.USER_FIRST_NAME);
                String lastName = resultSet.getString(DatabaseColumnName.USER_LAST_NAME);
                int defaultCurrency = Integer.parseInt(resultSet.getString(DatabaseColumnName.USER_DEFAULT_CURRENCY));
                String email = resultSet.getString(DatabaseColumnName.USER_EMAIL);
                Timestamp registrationDate = resultSet.getTimestamp(DatabaseColumnName.USER_REGISTRATION_DATE);
                // Create User objects and add them to the list
                return new User(id, login, passwordHash, firstName, lastName, defaultCurrency, email, registrationDate);
            }
        } catch (SQLException e) {
            logger.error("Error while retrieving a user by login from the database", e);
            throw new DaoException("Error while retrieving a user by login from the database", e);
        } catch (ConnectionPoolException e) {
            logger.fatal("Error while retrieving a user by login from the database", e);
            throw new RuntimeException("Error while retrieving a user by login from the database", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
        return null;
    }

    public boolean create(User user) throws DaoException {
        logger.info("Creating user: {}", user.getLogin());
        PreparedStatement statement;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(INSERT_USER);

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getLastName());
            statement.setInt(5, user.getDefaultCurrency());
            statement.setString(6, user.getEmail());
            statement.setTimestamp(7, new java.sql.Timestamp(user.getRegistrationDate().getTime()));
            int result = statement.executeUpdate();
            logger.debug("New user creation in database returned: {}", result);
            return result > 0;
        } catch (SQLException e) {
            logger.error("Error while adding a user to the database", e);
            throw new DaoException("Error while adding a user to the database", e);
        } catch (ConnectionPoolException e) {
            logger.fatal("Error while adding a user to the database", e);
            throw new RuntimeException("Error while adding a user to the database", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }

    @Override
    public boolean update(User user) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Update operation is not supported yet.");
    }

    @Override
    public boolean delete(User user) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Delete operation is not supported for User");
    }
}
