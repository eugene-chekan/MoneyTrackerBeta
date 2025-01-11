package lt.ehu.student.moneytracker.service;

import lt.ehu.student.moneytracker.model.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionService {
    Transaction save(Transaction transaction);
    Optional<Transaction> findById(UUID id);
    List<Transaction> findByUserId(Integer userId);
    List<Transaction> findByUserIdAndDateRange(Integer userId, LocalDateTime startDate, LocalDateTime endDate);
    void delete(UUID id);
} 