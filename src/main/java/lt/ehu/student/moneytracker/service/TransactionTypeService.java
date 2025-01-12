package lt.ehu.student.moneytracker.service;

import lt.ehu.student.moneytracker.model.TransactionType;
import java.util.List;
import java.util.Optional;

public interface TransactionTypeService {
    List<TransactionType> findAll();
    Optional<TransactionType> findById(Integer id);
    Optional<TransactionType> findByName(String name);
    TransactionType save(TransactionType type);
    TransactionType getById(Integer id);
    void delete(Integer id);
} 