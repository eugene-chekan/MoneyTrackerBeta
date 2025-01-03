package lt.ehu.student.moneytrackerbeta.dao.impl;

import lt.ehu.student.moneytrackerbeta.connection.ConnectionPool;
import lt.ehu.student.moneytrackerbeta.constant.DatabaseColumnName;
import lt.ehu.student.moneytrackerbeta.exception.ConnectionPoolException;
import lt.ehu.student.moneytrackerbeta.exception.DaoException;
import lt.ehu.student.moneytrackerbeta.model.TransactionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionTypeDao {
    private static final Logger logger = LogManager.getLogger(TransactionTypeDao.class.getName());
    private static final String SELECT_ALL = "SELECT " + DatabaseColumnName.TRANSACTION_TYPE_ID + ", " + DatabaseColumnName.TRANSACTION_TYPE_DESCRIPTION + ", " + DatabaseColumnName.TRANSACTION_TYPE_NAME + " FROM public.transaction_type ORDER BY " + DatabaseColumnName.TRANSACTION_TYPE_ID;
    private static final String SELECT_DISPLAYABLE = "SELECT " + DatabaseColumnName.TRANSACTION_TYPE_ID + ", " + DatabaseColumnName.TRANSACTION_TYPE_DESCRIPTION + ", " + DatabaseColumnName.TRANSACTION_TYPE_NAME + " FROM public.transaction_type WHERE " + DatabaseColumnName.TRANSACTION_TYPE_NAME + " != '" + TransactionType.ACCOUNT + "' ORDER BY " + DatabaseColumnName.TRANSACTION_TYPE_ID;
    private static final String SELECT_BY_NAME = "SELECT " + DatabaseColumnName.TRANSACTION_TYPE_ID + ", " + DatabaseColumnName.TRANSACTION_TYPE_DESCRIPTION + ", " + DatabaseColumnName.TRANSACTION_TYPE_NAME + " FROM public.transaction_type WHERE " + DatabaseColumnName.TRANSACTION_TYPE_NAME + " = ?";

    public List<TransactionType> findAll() throws DaoException {
        List<TransactionType> types = new ArrayList<>();
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                types.add(new TransactionType(
                    resultSet.getInt(DatabaseColumnName.TRANSACTION_TYPE_ID),
                    resultSet.getString(DatabaseColumnName.TRANSACTION_TYPE_DESCRIPTION),
                    resultSet.getString(DatabaseColumnName.TRANSACTION_TYPE_NAME)
                ));
            }
            return types;
        } catch (SQLException e) {
            logger.error("Error while retrieving transaction types", e);
            throw new DaoException("Error while retrieving transaction types", e);
        } catch (ConnectionPoolException e) {
            logger.fatal("Error while retrieving transaction types", e);
            throw new RuntimeException("Error while retrieving transaction types", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }

    public List<TransactionType> findDisplayable() throws DaoException {
        List<TransactionType> types = new ArrayList<>();
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_DISPLAYABLE);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                types.add(new TransactionType(
                    resultSet.getInt(DatabaseColumnName.TRANSACTION_TYPE_ID),
                    resultSet.getString(DatabaseColumnName.TRANSACTION_TYPE_DESCRIPTION),
                    resultSet.getString(DatabaseColumnName.TRANSACTION_TYPE_NAME)
                ));
            }
            return types;
        } catch (SQLException e) {
            logger.error("Error while retrieving transaction types", e);
            throw new DaoException("Error while retrieving transaction types", e);
        } catch (ConnectionPoolException e) {
            logger.fatal("Error while retrieving transaction types", e);
            throw new RuntimeException("Error while retrieving transaction types", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }

    public TransactionType findByName(String name) throws DaoException {
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_NAME);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new TransactionType(
                    resultSet.getInt(DatabaseColumnName.TRANSACTION_TYPE_ID),
                    resultSet.getString(DatabaseColumnName.TRANSACTION_TYPE_DESCRIPTION),
                    resultSet.getString(DatabaseColumnName.TRANSACTION_TYPE_NAME)
                );
            }
            return null;
        } catch (SQLException e) {
            logger.error("Error while retrieving transaction type", e);
            throw new DaoException("Error while retrieving transaction type", e);
        } catch (ConnectionPoolException e) {
            logger.fatal("Error while retrieving transaction type", e);
            throw new RuntimeException("Error while retrieving transaction type", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }
} 