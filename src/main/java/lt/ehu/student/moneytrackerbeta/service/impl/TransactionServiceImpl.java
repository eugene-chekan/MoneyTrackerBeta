package lt.ehu.student.moneytrackerbeta.service.impl;

import lt.ehu.student.moneytrackerbeta.dao.impl.AssetDao;
import lt.ehu.student.moneytrackerbeta.dao.impl.TransactionDao;
import lt.ehu.student.moneytrackerbeta.exception.DaoException;
import lt.ehu.student.moneytrackerbeta.exception.ServiceException;
import lt.ehu.student.moneytrackerbeta.model.Asset;
import lt.ehu.student.moneytrackerbeta.model.Transaction;
import lt.ehu.student.moneytrackerbeta.service.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = LogManager.getLogger(TransactionServiceImpl.class);

    @Override
    public boolean addTransaction(int userId, int type, String source, String destination, Timestamp date, BigDecimal amount, String comment) throws ServiceException {
        AssetDao assetDao = new AssetDao();
        TransactionDao transactionDao = new TransactionDao();
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        UUID sourceUUID = UUID.fromString(source);
        try {
            Asset sourceAsset = assetDao.findById(sourceUUID);
            transaction.setTypeId(type);
            transaction.setTimestamp(date);
            transaction.setSourceId(sourceUUID);
            transaction.setDestinationId(UUID.fromString(destination));
            transaction.setAmount(amount);
            transaction.setCurrencyId(sourceAsset.getCurrencyId());
            transaction.setComment(comment);
            return transactionDao.create(transaction);
        } catch (DaoException e) {
            logger.debug("Error while adding transaction to the database", e);
            throw new ServiceException("Error while adding transaction to the database", e);
        }
    }

    @Override
    public void deleteTransaction(int transactionId) {

    }

    @Override
    public void updateTransaction(int transactionId, String category, int type, double amount, String date) {

    }

    @Override
    public List<Transaction> findFilteredTransactions(int userId, int type, Timestamp fromDate, Timestamp toDate) throws ServiceException {
        TransactionDao transactionDao = new TransactionDao();
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setTypeId(type);
        transaction.setTimestamp(fromDate);
        transaction.setTimestamp(toDate);
        try {
            return transactionDao.findFilteredTransactions(userId, type, fromDate, toDate);
        } catch (DaoException e) {
            logger.debug("Error while retrieving filtered transactions from the database", e);
            throw new ServiceException("Error while retrieving filtered transactions from the database", e);
        }
    }

    @Override
    public double calculateTotalIncome() {
        return 0;
    }

    @Override
    public double calculateTotalExpenses() {
        return 0;
    }

    @Override
    public double calculateBalance() {
        return 0;
    }

    @Override
    public double calculateIncomeByCategory(String category) {
        return 0;
    }

    @Override
    public double calculateExpensesByCategory(String category) {
        return 0;
    }

    @Override
    public double calculateBalanceByCategory(String category) {
        return 0;
    }

    @Override
    public double calculateIncomeByDate(String date) {
        return 0;
    }

    @Override
    public double calculateExpensesByDate(String date) {
        return 0;
    }

    @Override
    public double calculateBalanceByDate(String date) {
        return 0;
    }
}
