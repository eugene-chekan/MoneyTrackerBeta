package lt.ehu.student.moneytracker.service.impl;

import lt.ehu.student.moneytracker.exception.ResourceNotFoundException;
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
    public TransactionType getById(Integer id) {
        return findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("TransactionType not found"));
    }

    @Override
    public Optional<TransactionType> findByName(String name) {
        return transactionTypeRepository.findByName(name);
    }

    @Override
    public TransactionType save(TransactionType type) {
        if (type.getId() == null && transactionTypeRepository.existsByName(type.getName())) {
            throw new IllegalArgumentException("TransactionType with this name already exists");
        }
        return transactionTypeRepository.save(type);
    }

    @Override
    public void delete(Integer id) {
        if (!transactionTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("TransactionType not found");
        }
        transactionTypeRepository.deleteById(id);
    }
} 