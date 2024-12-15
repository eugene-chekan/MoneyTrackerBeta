package lt.ehu.student.moneytrackerbeta.dao.impl;

import lt.ehu.student.moneytrackerbeta.connection.ConnectionPool;
import lt.ehu.student.moneytrackerbeta.constant.DatabaseColumnName;
import lt.ehu.student.moneytrackerbeta.dao.BaseDao;
import lt.ehu.student.moneytrackerbeta.exception.DaoException;
import lt.ehu.student.moneytrackerbeta.model.Asset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AssetDao implements BaseDao<Asset> {
    private static final Logger logger = LogManager.getLogger(AssetDao.class.getName());
    private static final String SELECT_ASSET_BY_ID = "SELECT " + DatabaseColumnName.ASSET_ID + ", " + DatabaseColumnName.ASSET_USER_ID + ", " + DatabaseColumnName.ASSET_TYPE_ID + ", " + DatabaseColumnName.ASSET_NAME + ", " + DatabaseColumnName.ASSET_BALANCE + ", " + DatabaseColumnName.ASSET_CURRENCY_ID + ", " + DatabaseColumnName.ASSET_DESCRIPTION + " FROM public.asset WHERE " + DatabaseColumnName.ASSET_ID + " = ?";
    private static final String SELECT_ASSETS_BY_USER_ID = "SELECT " + DatabaseColumnName.ASSET_ID + ", " + DatabaseColumnName.ASSET_USER_ID + ", " + DatabaseColumnName.ASSET_TYPE_ID + ", " + DatabaseColumnName.ASSET_NAME + ", " + DatabaseColumnName.ASSET_BALANCE + ", " + DatabaseColumnName.ASSET_CURRENCY_ID + ", " + DatabaseColumnName.ASSET_DESCRIPTION + " FROM public.asset WHERE " + DatabaseColumnName.ASSET_USER_ID + " = ?";
    private static final String SELECT_ASSETS_BY_USER_ID_AND_TYPE = "SELECT " + DatabaseColumnName.ASSET_ID + ", " + DatabaseColumnName.ASSET_USER_ID + ", " + DatabaseColumnName.ASSET_TYPE_ID + ", " + DatabaseColumnName.ASSET_NAME + ", " + DatabaseColumnName.ASSET_BALANCE + ", " + DatabaseColumnName.ASSET_CURRENCY_ID + ", " + DatabaseColumnName.ASSET_DESCRIPTION + " FROM public.asset WHERE " + DatabaseColumnName.ASSET_USER_ID + " = ? AND " + DatabaseColumnName.ASSET_TYPE_ID + " = ?";
    private static final String SELECT_ASSET_BY_USER_ID_AND_NAME = "SELECT " + DatabaseColumnName.ASSET_ID + ", " + DatabaseColumnName.ASSET_USER_ID + ", " + DatabaseColumnName.ASSET_TYPE_ID + ", " + DatabaseColumnName.ASSET_NAME + ", " + DatabaseColumnName.ASSET_BALANCE + ", " + DatabaseColumnName.ASSET_CURRENCY_ID + ", " + DatabaseColumnName.ASSET_DESCRIPTION + " FROM public.asset WHERE " + DatabaseColumnName.ASSET_USER_ID + " = ? AND " + DatabaseColumnName.ASSET_NAME + " = ?";
    private static final String INSERT_ASSET = "INSERT INTO public.asset (" + DatabaseColumnName.ASSET_ID + ", " + DatabaseColumnName.ASSET_USER_ID + ", " + DatabaseColumnName.ASSET_TYPE_ID + ", " + DatabaseColumnName.ASSET_NAME + ", " + DatabaseColumnName.ASSET_BALANCE + ", " + DatabaseColumnName.ASSET_CURRENCY_ID + ", " + DatabaseColumnName.ASSET_DESCRIPTION + ") VALUES (?, ?, ?, ?, ?, ?, ?)";
    @Override
    public boolean create(Asset asset) throws DaoException, SQLException {
        logger.info("Creating new asset: {}", asset.getName());
        PreparedStatement statement;
        Connection connection = null;
        // Implement the logic to add a user to the database
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(INSERT_ASSET);

            statement.setObject(1, asset.getId());
            statement.setInt(2, asset.getUserId());
            statement.setInt(3, asset.getTypeId());
            statement.setString(4, asset.getName());
            statement.setBigDecimal(5, asset.getBalance());
            statement.setInt(6, asset.getCurrencyId());
            statement.setString(7, asset.getDescription());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error("Error while adding new asset to the database", e);
            throw new DaoException("Error while adding new asset to the database", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }

    @Override
    public boolean update(Asset asset) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(Asset asset) throws DaoException {
        return false;
    }

    @Override
    public List<Asset> findAll() throws DaoException, SQLException {
        return List.of();
    }

    @Override
    public Asset findById(int id) throws DaoException {
        return null;
    }

    public List<Asset> findAllByUserId(int userId) throws DaoException, SQLException {
        logger.info("Finding all assets by user ID: {}", userId);
        PreparedStatement statement;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SELECT_ASSETS_BY_USER_ID);
            statement.setInt(1, userId);
            return getAssets(statement);
        } catch (SQLException e) {
            logger.error("Error while retrieving assets from the database", e);
            throw new DaoException("Error while retrieving assets from the database", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }

    @Override
    public Asset findById(UUID id) throws DaoException {
        logger.debug("Finding asset by ID: {}", id);
        PreparedStatement statement;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SELECT_ASSET_BY_ID);
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Asset(
                        resultSet.getObject(DatabaseColumnName.ASSET_ID, UUID.class),
                        resultSet.getInt(DatabaseColumnName.ASSET_USER_ID),
                        resultSet.getInt(DatabaseColumnName.ASSET_TYPE_ID),
                        resultSet.getString(DatabaseColumnName.ASSET_NAME),
                        resultSet.getBigDecimal(DatabaseColumnName.ASSET_BALANCE),
                        resultSet.getInt(DatabaseColumnName.ASSET_CURRENCY_ID),
                        resultSet.getString(DatabaseColumnName.ASSET_DESCRIPTION)
                );
            }
        } catch (SQLException e) {
            logger.error("Error while retrieving asset from the database", e);
            throw new DaoException("Error while retrieving asset from the database", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
        return null;
    }

    public List<Asset> findAllByUserIdAndType(int userId, int typeId) throws DaoException {
        logger.debug("Finding all assets by user ID and type: {}, {}", userId, typeId);
        PreparedStatement statement;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SELECT_ASSETS_BY_USER_ID_AND_TYPE);
            statement.setInt(1, userId);
            statement.setInt(2, typeId);
            return getAssets(statement);
        } catch (SQLException e) {
            logger.error("Error while retrieving assets from the database", e);
            throw new DaoException("Error while retrieving assets from the database", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }

    public Asset findByUserIdAndName(int userId, String name) throws DaoException {
        logger.debug("Finding asset by user ID and name: {}, {}", userId, name);
        PreparedStatement statement;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            statement = connection.prepareStatement(SELECT_ASSET_BY_USER_ID_AND_NAME);
            statement.setInt(1, userId);
            statement.setString(2, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Asset(
                        resultSet.getObject(DatabaseColumnName.ASSET_ID, UUID.class),
                        resultSet.getInt(DatabaseColumnName.ASSET_USER_ID),
                        resultSet.getInt(DatabaseColumnName.ASSET_TYPE_ID),
                        resultSet.getString(DatabaseColumnName.ASSET_NAME),
                        resultSet.getBigDecimal(DatabaseColumnName.ASSET_BALANCE),
                        resultSet.getInt(DatabaseColumnName.ASSET_CURRENCY_ID),
                        resultSet.getString(DatabaseColumnName.ASSET_DESCRIPTION)
                );
            }
        } catch (SQLException e) {
            logger.error("Error while retrieving asset from the database", e);
            throw new DaoException("Error while retrieving asset from the database", e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
        return null;
    }

    private List<Asset> getAssets(PreparedStatement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery();
        List<Asset> assets = new ArrayList<>();
        while (resultSet.next()) {
            Asset asset = new Asset(
                    resultSet.getObject(DatabaseColumnName.ASSET_ID, UUID.class),
                    resultSet.getInt(DatabaseColumnName.ASSET_USER_ID),
                    resultSet.getInt(DatabaseColumnName.ASSET_TYPE_ID),
                    resultSet.getString(DatabaseColumnName.ASSET_NAME),
                    resultSet.getBigDecimal(DatabaseColumnName.ASSET_BALANCE),
                    resultSet.getInt(DatabaseColumnName.ASSET_CURRENCY_ID),
                    resultSet.getString(DatabaseColumnName.ASSET_DESCRIPTION)
            );
            assets.add(asset);
        }
        return assets;
    }
}
