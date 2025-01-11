package lt.ehu.student.moneytracker.service.impl;

import lt.ehu.student.moneytracker.model.Transaction;
import lt.ehu.student.moneytracker.repository.TransactionRepository;
import lt.ehu.student.moneytracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> findByUserId(Integer userId) {
        return transactionRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    @Override
    public List<Transaction> findByUserIdAndDateRange(Integer userId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByUserIdAndTimestampBetweenOrderByTimestampDesc(userId, startDate, endDate);
    }

    @Override
    public void delete(UUID id) {
        transactionRepository.deleteById(id);
    }
} 