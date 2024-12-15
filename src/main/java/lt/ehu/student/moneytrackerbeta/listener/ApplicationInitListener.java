package lt.ehu.student.moneytrackerbeta.listener;

import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.annotation.WebListener;
import lt.ehu.student.moneytrackerbeta.dao.impl.CurrencyDao;
import lt.ehu.student.moneytrackerbeta.dao.impl.TransactionTypeDao;
import lt.ehu.student.moneytrackerbeta.exception.DaoException;
import lt.ehu.student.moneytrackerbeta.model.Currency;
import lt.ehu.student.moneytrackerbeta.model.TransactionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@WebListener
public class ApplicationInitListener implements ServletContextListener {
    private static final Logger logger = LogManager.getLogger(ApplicationInitListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Wait for connection pool to be initialized
        if (sce.getServletContext().getAttribute("connectionPoolInitialized") == null) {
            logger.error("Connection pool not initialized");
            throw new IllegalStateException("Connection pool must be initialized before application initialization");
        }

        try {
            CurrencyDao currencyDao = new CurrencyDao();
            TransactionTypeDao transactionTypeDao = new TransactionTypeDao();
            
            List<Currency> currencies = currencyDao.findAll();
            List<TransactionType> types = transactionTypeDao.findDisplayable();
            
            sce.getServletContext().setAttribute("availableCurrencies", currencies);
            sce.getServletContext().setAttribute("availableTransactionTypes", types);
            
            logger.info("Application data initialized successfully");
        } catch (DaoException e) {
            logger.error("Error initializing application data", e);
            throw new RuntimeException("Error initializing application data", e);
        }
    }
} 