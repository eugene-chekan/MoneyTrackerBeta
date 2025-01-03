package lt.ehu.student.moneytrackerbeta.controller.command.impl;

import lt.ehu.student.moneytrackerbeta.constant.PagePath;
import lt.ehu.student.moneytrackerbeta.controller.command.Command;
import jakarta.servlet.http.HttpServletRequest;
import lt.ehu.student.moneytrackerbeta.dao.impl.CurrencyDao;
import lt.ehu.student.moneytrackerbeta.exception.CommandException;
import lt.ehu.student.moneytrackerbeta.exception.DaoException;
import lt.ehu.student.moneytrackerbeta.model.Currency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ShowSignUpPageCommand implements Command {
    private static final Logger logger = LogManager.getLogger(ShowSignUpPageCommand.class.getName());

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        CurrencyDao currencyDao = new CurrencyDao();
        try {
            List<Currency> currencies = currencyDao.findAll();
            request.setAttribute("currencies", currencies);
        } catch (DaoException e) {
            logger.error("Error fetching currencies", e);
            throw new CommandException("Error fetching currencies", e);
        }
        return PagePath.SIGNUP;
    }
} 