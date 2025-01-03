package lt.ehu.student.moneytrackerbeta.dao.impl;

import lt.ehu.student.moneytrackerbeta.connection.ConnectionPool;
import lt.ehu.student.moneytrackerbeta.constant.DatabaseColumnName;
import lt.ehu.student.moneytrackerbeta.dao.BaseDao;
import lt.ehu.student.moneytrackerbeta.exception.ConnectionPoolException;
import lt.ehu.student.moneytrackerbeta.exception.DaoException;
import lt.ehu.student.moneytrackerbeta.model.Transaction;
import lt.ehu.student.moneytrackerbeta.model.dto.TransactionDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class TransactionDao implements BaseDao<Transaction> {
    private static final Logger logger = LogManager.getLogger(TransactionDao.class.getName());
    private static final String INSERT_TRANSACTION = "INSERT INTO transaction (" + DatabaseColumnName.TRANSACTION_ID + ", " + DatabaseColumnName.TRANSACTION_USER_ID + ", " + DatabaseColumnName.TRANSACTION_TYPE + ", " + DatabaseColumnName.TRANSACTION_TIMESTAMP + ", " + DatabaseColumnName.TRANSACTION_SOURCE + ", " + DatabaseColumnName.TRANSACTION_DESTINATION + ", " + DatabaseColumnName.TRANSACTION_AMOUNT + ", " + DatabaseColumnName.TRANSACTION_CURRENCY + ", " + DatabaseColumnName.TRANSACTION_COMMENT + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_FILTERED_TRANSACTIONS = """
        SELECT 
            t.id,
            t.user_id,
            tt.name as transaction_type,
            t.timestamp,
            src.name as source_name,
            dst.name as destination_name,
            t.amount,
            c.symbol as currency_symbol,
            t.comment
        FROM transaction t
        JOIN transaction_type tt ON t.type = tt.id
        JOIN currency c ON t.currency_id = c.id
        LEFT JOIN asset src ON t.source = src.id
        LEFT JOIN asset dst ON t.destination = dst.id
        WHERE t.user_id = ? 
            AND (? IS NULL OR t.type = ?)
            AND t.timestamp >= ? 
            AND t.timestamp <= ?
        ORDER BY t.timestamp DESC
    """;

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
        } catch (ConnectionPoolException e) {
            logger.fatal("Error while creating transaction in the database", e);
            throw new RuntimeException("Error while creating transaction in the database", e);
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

    public List<TransactionDto> findFilteredTransactions(int userId, int type, Timestamp fromDate, Timestamp toDate) throws DaoException {
        PreparedStatement statement;
        Connection connection = null;
        List<TransactionDto> transactions = new java.util.ArrayList<>();
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
                transactions.add(new TransactionDto(
                    resultSet.getObject("id", UUID.class),
                    resultSet.getInt("user_id"),
                    resultSet.getString("transaction_type"),
                    resultSet.getTimestamp("timestamp"),
                    resultSet.getString("source_name"),
                    resultSet.getString("destination_name"),
                    resultSet.getBigDecimal("amount"),
                    resultSet.getString("currency_symbol"),
                    resultSet.getString("comment")
                ));
            }
            if (transactions.isEmpty()) {
                logger.debug("No transactions found for the given criteria");
            } else {
                logger.debug("Transactions found: {}", transactions.size());
            }
            return transactions;
        } catch (SQLException e) {
            logger.error("Error while retrieving filtered transactions from the database", e);
            throw new DaoException("Error while retrieving filtered transactions from the database", e);
        } catch (ConnectionPoolException e) {
            logger.fatal("Error while retrieving filtered transactions from the database", e);
            throw new RuntimeException("Error while retrieving filtered transactions from the database", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }
}
