package lt.ehu.student.moneytracker.repository;

import lt.ehu.student.moneytracker.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUserIdOrderByTimestampDesc(Integer userId);
    List<Transaction> findByUserIdAndTimestampBetweenOrderByTimestampDesc(
            Integer userId, 
            LocalDateTime startDate, 
            LocalDateTime endDate
    );
} 