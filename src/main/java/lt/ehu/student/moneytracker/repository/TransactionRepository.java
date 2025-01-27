package lt.ehu.student.moneytracker.repository;

import lt.ehu.student.moneytracker.model.Transaction;
import lt.ehu.student.moneytracker.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUserOrderByTimestampDesc(User user);
    List<Transaction> findByUserAndTimestampBetweenOrderByTimestampDesc(
            User user, 
            LocalDateTime startDate, 
            LocalDateTime endDate
    );
    
    // For reporting
    @Query("SELECT t.currency, SUM(t.amount) FROM Transaction t " +
           "WHERE t.user = :user AND t.timestamp BETWEEN :startDate AND :endDate " +
           "GROUP BY t.currency")
    List<Object[]> sumAmountByCurrency(User user, LocalDateTime startDate, LocalDateTime endDate);
    Optional<Transaction> findByIdAndUserId(UUID id, Integer userId);
}