package lt.ehu.student.moneytracker.service.impl;

import lt.ehu.student.moneytracker.exception.ResourceNotFoundException;
import lt.ehu.student.moneytracker.model.Transaction;
import lt.ehu.student.moneytracker.model.User;
import lt.ehu.student.moneytracker.repository.TransactionRepository;
import lt.ehu.student.moneytracker.service.TransactionService;
import lt.ehu.student.moneytracker.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
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
    public Transaction getById(UUID id) {
        return findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    @Override
    public List<Transaction> findByUserId(Integer userId) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return transactionRepository.findByUserOrderByTimestampDesc(user);
    }

    @Override
    public List<Transaction> findByUserIdAndDateRange(Integer userId, LocalDateTime startDate, LocalDateTime endDate) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return transactionRepository.findByUserAndTimestampBetweenOrderByTimestampDesc(user, startDate, endDate);
    }

    @Override
    public void delete(UUID id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found");
        }
        transactionRepository.deleteById(id);
    }

    @Override
    public void validateUserAccess(UUID transactionId, Integer userId) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Transaction transaction = getById(transactionId);
        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User does not have access to this transaction");
        }
    }

    @Override
    public Optional<Transaction> findByIdAndUserId(UUID id, Integer userId) {
        return transactionRepository.findByIdAndUserId(id, userId);
    }
} 