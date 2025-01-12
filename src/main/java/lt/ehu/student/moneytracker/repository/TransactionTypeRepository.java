package lt.ehu.student.moneytracker.repository;

import lt.ehu.student.moneytracker.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Integer> {
    Optional<TransactionType> findByName(String name);
    boolean existsByName(String name);
    
    @Query("SELECT tt FROM TransactionType tt LEFT JOIN FETCH tt.transactions " +
           "WHERE tt.id = :id")
    Optional<TransactionType> findByIdWithTransactions(Integer id);
    
    @Query("SELECT DISTINCT tt FROM TransactionType tt " +
           "LEFT JOIN tt.transactions t " +
           "WHERE t.user.id = :userId")
    List<TransactionType> findByUserId(Integer userId);
    
    @Query("SELECT tt FROM TransactionType tt " +
           "WHERE EXISTS (SELECT t FROM Transaction t WHERE t.type = tt)")
    List<TransactionType> findUsedTypes();
} 