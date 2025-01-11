package lt.ehu.student.moneytracker.service.impl;

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
    public Optional<Currency> findByCode(String code) {
        return currencyRepository.findByCode(code);
    }

    @Override
    public Currency save(Currency currency) {
        return currencyRepository.save(currency);
    }

    @Override
    public void delete(Integer id) {
        currencyRepository.deleteById(id);
    }

    @Override
    public long count() {
        return currencyRepository.count();
    }
} 