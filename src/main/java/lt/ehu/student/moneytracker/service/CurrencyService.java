package lt.ehu.student.moneytracker.service;

import lt.ehu.student.moneytracker.model.Currency;
import java.util.List;
import java.util.Optional;

public interface CurrencyService {
    List<Currency> findAll();
    Optional<Currency> findById(Integer id);
    Optional<Currency> findByCode(String code);
    Currency getById(Integer id);
    Currency save(Currency currency);
    void delete(Integer id);
    long count();
} 