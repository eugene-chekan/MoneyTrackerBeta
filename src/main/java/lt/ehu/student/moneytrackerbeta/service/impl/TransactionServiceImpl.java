package lt.ehu.student.moneytrackerbeta.service.impl;

import lt.ehu.student.moneytrackerbeta.dao.impl.AssetDao;
import lt.ehu.student.moneytrackerbeta.dao.impl.TransactionDao;
import lt.ehu.student.moneytrackerbeta.exception.DaoException;
import lt.ehu.student.moneytrackerbeta.exception.ServiceException;
import lt.ehu.student.moneytrackerbeta.model.Asset;
import lt.ehu.student.moneytrackerbeta.model.Transaction;
import lt.ehu.student.moneytrackerbeta.service.TransactionService;

import java.math.BigDecimal;

public class TransactionServiceImpl implements TransactionService {
    @Override
    public boolean addTransaction(int userId, String type, String source, String destination, String label, String date, BigDecimal amount, String comment, String currency) throws ServiceException {
        TransactionDao transactionDao = new TransactionDao();
        AssetDao assetDao = new AssetDao();
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        try {
            int transactionTypeId = transactionDao.findTypeByName(type);
            Asset sourceAsset = assetDao.findByUserIdAndName(userId, source);
            Asset destinationAsset = assetDao.findByUserIdAndName(userId, destination);
            transaction.setType(transactionTypeId);
            transaction.setSource(sourceAsset.getId());
            transaction.setDestination(destinationAsset.getId());
            transaction.setLabel(label);
            transaction.setDate(date);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTransaction(int transactionId) {

    }

    @Override
    public void updateTransaction(int transactionId, String category, String type, double amount, String date) {

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
