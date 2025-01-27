package lt.ehu.student.moneytracker.service.impl;

import lt.ehu.student.moneytracker.exception.ResourceNotFoundException;
import lt.ehu.student.moneytracker.model.Currency;
import lt.ehu.student.moneytracker.repository.CurrencyRepository;
import lt.ehu.student.moneytracker.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    @Override
    public Optional<Currency> findById(Integer id) {
        return currencyRepository.findById(id);
    }

    @Override
    public Currency getById(Integer id) {
        return findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Currency not found"));
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        return currencyRepository.findByCode(code);
    }

    @Override
    public Currency save(Currency currency) {
        if (currency.getId() == null && currencyRepository.existsByCode(currency.getCode())) {
            throw new IllegalArgumentException("Currency with this code already exists");
        }
        return currencyRepository.save(currency);
    }

    @Override
    public void delete(Integer id) {
        if (!currencyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Currency not found");
        }
        currencyRepository.deleteById(id);
    }

    @Override
    public long count() {
        return currencyRepository.count();
    }
} 