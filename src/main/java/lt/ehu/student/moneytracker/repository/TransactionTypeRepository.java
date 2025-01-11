package lt.ehu.student.moneytracker.repository;

import lt.ehu.student.moneytracker.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Integer> {
    Optional<TransactionType> findByName(String name);
    boolean existsByName(String name);
} 