package lt.ehu.student.moneytracker.repository;

import lt.ehu.student.moneytracker.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
    Optional<Currency> findByCode(String code);
    boolean existsByCode(String code);
    // List<Currency> findByEnabled(boolean enabled);
} 