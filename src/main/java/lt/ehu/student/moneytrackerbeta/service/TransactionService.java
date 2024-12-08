package lt.ehu.student.moneytrackerbeta.service;

import lt.ehu.student.moneytrackerbeta.exception.ServiceException;
import lt.ehu.student.moneytrackerbeta.model.dto.TransactionDto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface TransactionService {
    boolean addTransaction(int userId, int type, String source, String destination, Timestamp date, BigDecimal amount, String comment) throws ServiceException;

    void deleteTransaction(int transactionId);

    void updateTransaction(int transactionId, String category, int type, double amount, String date);

    List<TransactionDto> findFilteredTransactions(int userId, int type, Timestamp fromDate, Timestamp toDate) throws ServiceException;

    double calculateTotalIncome();

    double calculateTotalExpenses();

    double calculateBalance();

    double calculateIncomeByCategory(String category);

    double calculateExpensesByCategory(String category);

    double calculateBalanceByCategory(String category);

    double calculateIncomeByDate(String date);

    double calculateExpensesByDate(String date);

    double calculateBalanceByDate(String date);
}
