package lt.ehu.student.moneytrackerbeta.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lt.ehu.student.moneytrackerbeta.dao.impl.AssetDao;
import lt.ehu.student.moneytrackerbeta.dao.impl.TransactionTypeDao;
import lt.ehu.student.moneytrackerbeta.dao.impl.UserDao;
import lt.ehu.student.moneytrackerbeta.exception.DaoException;
import lt.ehu.student.moneytrackerbeta.exception.ServiceException;
import lt.ehu.student.moneytrackerbeta.model.Asset;
import lt.ehu.student.moneytrackerbeta.model.TransactionType;
import lt.ehu.student.moneytrackerbeta.model.User;
import lt.ehu.student.moneytrackerbeta.service.UserService;
import lt.ehu.student.moneytrackerbeta.utility.PasswordUtil;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class.getName());

    @Override
    public boolean verifyLogin(String username, String password) throws ServiceException {
        UserDao userDao = new UserDao();
        User user;
        try {
            user = userDao.findByLogin(username);
        } catch (DaoException e) {
            logger.error(String.format("Error while verifying login: %s", username), e);
            throw new ServiceException("Error while verifying login", e);
        }
        if (user == null) {
            return false;
        }
        // Verify the password using the PasswordUtil class
        return PasswordUtil.checkPassword(password, user.getPasswordHash());
    }

    @Override
    public boolean registerUser(String username, String password, String firstName, String lastName,
            int defaultCurrency, String email) throws ServiceException {
        User user = new User();
        if (isUsernameTaken(username)) {
            logger.error(String.format("Username already taken: %s", username));
            return false;
        }
        // Hash the password before storing it in the database
        String hashedPass = PasswordUtil.hashPassword(password);
        user.setLogin(username);
        user.setPasswordHash(hashedPass);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDefaultCurrency(defaultCurrency);
        user.setEmail(email);
        user.setRegistrationDate(new Timestamp(System.currentTimeMillis()));
        UserDao userDao = new UserDao();
        TransactionTypeDao transactionTypeDao = new TransactionTypeDao();
        try {
            boolean userCreated = userDao.create(user);
            user = userDao.findByLogin(username);
            // Create default assets for the user
            List<TransactionType> types = transactionTypeDao.findAll();
            createDefaultAssets(user, types);
            logger.info(String.format("User created successfully: %s", username));
            return userCreated;
        } catch (DaoException e) {
            logger.error(String.format("Error while registering user: %s", username), e);
            throw new ServiceException("Error while registering user", e);
        }
    }

    @Override
    public boolean isUsernameTaken(String username) throws ServiceException {
        UserDao userDao = new UserDao();
        try {
            return userDao.findByLogin(username) != null;
        } catch (DaoException e) {
            throw new ServiceException("Error while checking if username is taken", e);
        }
    }

    @Override
    public String getFirstName(String username) throws ServiceException {
        UserDao userDao = new UserDao();
        try {
            return userDao.findByLogin(username).getFirstName();
        } catch (DaoException e) {
            throw new ServiceException("Error while getting first name", e);
        }
    }

    @Override
    public int getUserId(String username) throws ServiceException {
        UserDao userDao = new UserDao();
        try {
            return userDao.findByLogin(username).getId();
        } catch (DaoException e) {
            throw new ServiceException("Error while getting user ID", e);
        }
    }

    @Override
    public List<Asset> findAssets(int userId) throws ServiceException {
        AssetDao assetDao = new AssetDao();
        try {
            return assetDao.findAllByUserId(userId);
        } catch (DaoException | SQLException e) {
            throw new ServiceException("Error while getting assets", e);
        }
    }

    @Override
    public User findUser(String username) throws ServiceException {
        UserDao userDao = new UserDao();
        try {
            return userDao.findByLogin(username);
        } catch (DaoException e) {
            throw new ServiceException("Error while finding user", e);
        }
    }

    public List<Asset> findAssetsByType(int userId, int type) throws ServiceException {
        AssetDao assetDao = new AssetDao();
        try {
            return assetDao.findAllByUserIdAndType(userId, type);
        } catch (DaoException e) {
            throw new ServiceException("Error while getting assets", e);
        }
    }

    private void createDefaultAssets(User user, List<TransactionType> types) throws ServiceException {
        AssetDao assetDao = new AssetDao();
        Asset defaultAccount = new Asset();
        Asset defaultExpense = new Asset();
        Asset defaultIncome = new Asset();

        defaultAccount.setUserId(user.getId());
        defaultAccount.setName("Cash (default)");
        defaultAccount.setCurrencyId(user.getDefaultCurrency());
        defaultAccount.setTypeId(types.stream().filter(type -> type.getName().equals(TransactionType.ACCOUNT))
                .findFirst().orElse(null).getId());

        defaultIncome.setUserId(user.getId());
        defaultIncome.setName("Salary (default)");
        defaultIncome.setCurrencyId(user.getDefaultCurrency());
        defaultIncome.setTypeId(types.stream().filter(type -> type.getName().equals(TransactionType.INCOME)).findFirst()
                .orElse(null).getId());

        defaultExpense.setUserId(user.getId());
        defaultExpense.setName("Groceries (default)");
        defaultExpense.setCurrencyId(user.getDefaultCurrency());
        defaultExpense.setTypeId(types.stream().filter(type -> type.getName().equals(TransactionType.EXPENSE))
                .findFirst().orElse(null).getId());
        try {
            assetDao.create(defaultAccount);
            assetDao.create(defaultIncome);
            assetDao.create(defaultExpense);
        } catch (DaoException | SQLException e) {
            throw new ServiceException("Error while creating default assets", e);
        }
    }
}
