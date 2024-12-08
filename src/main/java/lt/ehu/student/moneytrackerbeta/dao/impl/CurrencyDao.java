package lt.ehu.student.moneytrackerbeta.dao.impl;

import lt.ehu.student.moneytrackerbeta.connection.ConnectionPool;
import lt.ehu.student.moneytrackerbeta.dao.BaseDao;
import lt.ehu.student.moneytrackerbeta.exception.DaoException;
import lt.ehu.student.moneytrackerbeta.model.Currency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CurrencyDao implements BaseDao<Currency> {
    private static final Logger logger = LogManager.getLogger(CurrencyDao.class);
    private static final String SELECT_ALL_CURRENCIES = "SELECT id, code, symbol, name FROM public.currency ORDER BY code";
    private static final String SELECT_CURRENCY_BY_ID = "SELECT id, code, symbol, name FROM public.currency WHERE id = ?";

    @Override
    public List<Currency> findAll() throws DaoException {
        List<Currency> currencies = new ArrayList<>();
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_CURRENCIES);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                currencies.add(new Currency(
                    resultSet.getInt("id"),
                    resultSet.getString("code"),
                    resultSet.getString("symbol"),
                    resultSet.getString("name")
                ));
            }
            return currencies;
        } catch (SQLException e) {
            logger.error("Error while retrieving currencies from database", e);
            throw new DaoException("Error while retrieving currencies from database", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }

    @Override
    public Currency findById(int id) throws DaoException {
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_CURRENCY_BY_ID);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Currency(
                    resultSet.getInt("id"),
                    resultSet.getString("code"),
                    resultSet.getString("symbol"),
                    resultSet.getString("name")
                );
            }
            return null;
        } catch (SQLException e) {
            logger.error("Error while retrieving currency from database", e);
            throw new DaoException("Error while retrieving currency from database", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }

    // Other required BaseDao methods with default implementations
    @Override
    public boolean create(Currency currency) throws DaoException { return false; }

    @Override
    public boolean update(Currency currency) throws DaoException { return false; }

    @Override
    public boolean delete(Currency currency) throws DaoException { return false; }

    @Override
    public Currency findById(UUID id) throws DaoException { return null; }
}