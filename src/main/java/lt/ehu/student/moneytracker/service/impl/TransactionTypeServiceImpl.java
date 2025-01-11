package lt.ehu.student.moneytracker.service.impl;

import lt.ehu.student.moneytracker.model.TransactionType;
import lt.ehu.student.moneytracker.repository.TransactionTypeRepository;
import lt.ehu.student.moneytracker.service.TransactionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransactionTypeServiceImpl implements TransactionTypeService {
    private final TransactionTypeRepository transactionTypeRepository;

    @Autowired
    public TransactionTypeServiceImpl(TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
    }

    @Override
    public List<TransactionType> findAll() {
        return transactionTypeRepository.findAll();
    }

    @Override
    public Optional<TransactionType> findById(Integer id) {
        return transactionTypeRepository.findById(id);
    }

    @Override
    public Optional<TransactionType> findByName(String name) {
        return transactionTypeRepository.findByName(name);
    }

    @Override
    public TransactionType save(TransactionType type) {
        return transactionTypeRepository.save(type);
    }

    @Override
    public void delete(Integer id) {
        transactionTypeRepository.deleteById(id);
    }
} 