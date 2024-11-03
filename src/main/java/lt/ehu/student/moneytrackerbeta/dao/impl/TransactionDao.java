package lt.ehu.student.moneytrackerbeta.dao.impl;

import lt.ehu.student.moneytrackerbeta.connection.ConnectionPool;
import lt.ehu.student.moneytrackerbeta.dao.BaseDao;
import lt.ehu.student.moneytrackerbeta.exception.DaoException;
import lt.ehu.student.moneytrackerbeta.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TransactionDao implements BaseDao<Transaction> {
    private static final String SELECT_TRANSACTION_TYPE_BY_NAME = "SELECT id, name, description FROM tr_type WHERE name = ?";
    private static final String INSERT_TRANSACTION = "INSERT INTO transaction (user_id, type_id, source_id, destination_id, label, date, amount, comment, currency) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    @Override
    public boolean create(Transaction transaction) throws DaoException, SQLException {
        return false;
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
}
