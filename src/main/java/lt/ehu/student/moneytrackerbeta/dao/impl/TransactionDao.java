package lt.ehu.student.moneytrackerbeta.dao.impl;

import lt.ehu.student.moneytrackerbeta.connection.ConnectionPool;
import lt.ehu.student.moneytrackerbeta.dao.BaseDao;
import lt.ehu.student.moneytrackerbeta.exception.DaoException;
import lt.ehu.student.moneytrackerbeta.model.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class TransactionDao implements BaseDao<Transaction> {
    private static final Logger logger = LogManager.getLogger(TransactionDao.class);
    private static final String SELECT_TRANSACTION_TYPE_BY_NAME = "SELECT id, name, description FROM public.transaction_type WHERE name = ?";
    private static final String INSERT_TRANSACTION = "INSERT INTO transaction (id, user_id, type, timestamp, source, destination, amount, currency_id, comment) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_FILTERED_TRANSACTIONS = "SELECT * FROM transaction WHERE user_id = ? AND (? IS NULL OR type = ?) AND timestamp >= ? AND timestamp <= ? ORDER BY timestamp DESC";

    @Override
    public boolean create(Transaction transaction) throws DaoException {
        PreparedStatement statement;
        Connection connection = null;
        try {
            int parameterIndex = 1;
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(INSERT_TRANSACTION);
            statement.setObject(parameterIndex++, transaction.getId());
            statement.setInt(parameterIndex++, transaction.getUserId());
            statement.setInt(parameterIndex++, transaction.getTypeId());
            statement.setTimestamp(parameterIndex++, transaction.getTimestamp());
            statement.setObject(parameterIndex++, transaction.getSourceId());
            statement.setObject(parameterIndex++, transaction.getDestinationId());
            statement.setBigDecimal(parameterIndex++, transaction.getAmount());
            statement.setInt(parameterIndex++, transaction.getCurrencyId());
            statement.setString(parameterIndex++, transaction.getComment());
            int rowsCreated = statement.executeUpdate();
            return rowsCreated > 0;
        } catch (SQLException e) {
            logger.error("Error while creating transaction in the database", e);
            throw new DaoException("Error while creating transaction in the database", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }

    @Override
    public boolean update(Transaction transaction) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(Transaction transaction) throws DaoException {
        return false;
    }

    @Override
    public List<Transaction> findAll() throws DaoException, SQLException {
        return List.of();
    }

    @Override
    public Transaction findById(int id) throws DaoException {
        return null;
    }

    @Override
    public Transaction findById(UUID id) throws DaoException {
        return null;
    }

    public int findTypeByName(String name) throws DaoException {
        PreparedStatement statement;
        Connection connection = null;
        int transactionTypeId = -1;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SELECT_TRANSACTION_TYPE_BY_NAME);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                transactionTypeId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            throw new DaoException("Error while retrieving transaction type from the database", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
        return transactionTypeId;
    }

    public List<Transaction> findFilteredTransactions(int userId, int type, Timestamp fromDate, Timestamp toDate) throws DaoException {
        PreparedStatement statement;
        Connection connection = null;
        List<Transaction> transactions = new java.util.ArrayList<>();
        try {
            int parameterIndex = 1;
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SELECT_FILTERED_TRANSACTIONS);
            statement.setInt(parameterIndex++, userId);
            if (type == 0) {
                statement.setNull(parameterIndex++, Types.INTEGER);
                statement.setNull(parameterIndex++, Types.INTEGER);
            } else {
                statement.setInt(parameterIndex++, type);
                statement.setInt(parameterIndex++, type);
            }
            statement.setTimestamp(parameterIndex++, fromDate);
            statement.setTimestamp(parameterIndex++, toDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getObject("id", UUID.class),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("type"),
                        resultSet.getTimestamp("timestamp"),
                        (UUID) resultSet.getObject("source"),
                        (UUID) resultSet.getObject("destination"),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getInt("currency_id"),
                        resultSet.getString("comment")
                ));
            }
            if (transactions.isEmpty()) {
                logger.debug("No transactions found for the given criteria");
            }
            else {
                logger.debug("Transactions found: {}", transactions.size());
            }
            return transactions;
        } catch (SQLException e) {
            logger.error("Error while retrieving filtered transactions from the database", e);
            throw new DaoException("Error while retrieving filtered transactions from the database", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }
}
