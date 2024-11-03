package lt.ehu.student.moneytrackerbeta.service;

import lt.ehu.student.moneytrackerbeta.exception.ServiceException;

import java.math.BigDecimal;

public interface TransactionService {
    boolean addTransaction(int userId, String type, String source, String destination, String label, String date, BigDecimal amount, String comment, String currency) throws ServiceException;
    void deleteTransaction(int transactionId);
    void updateTransaction(int transactionId, String category, String type, double amount, String date);
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
