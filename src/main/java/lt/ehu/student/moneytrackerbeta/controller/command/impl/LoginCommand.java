package lt.ehu.student.moneytrackerbeta.controller.command.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lt.ehu.student.moneytrackerbeta.controller.command.Command;
import lt.ehu.student.moneytrackerbeta.exception.ServiceException;
import lt.ehu.student.moneytrackerbeta.model.Asset;
import lt.ehu.student.moneytrackerbeta.model.Currency;
import lt.ehu.student.moneytrackerbeta.model.TransactionType;
import lt.ehu.student.moneytrackerbeta.model.User;
import lt.ehu.student.moneytrackerbeta.service.UserService;
import lt.ehu.student.moneytrackerbeta.service.impl.UserServiceImpl;
import lt.ehu.student.moneytrackerbeta.dao.impl.CurrencyDao;
import lt.ehu.student.moneytrackerbeta.exception.CommandException;
import lt.ehu.student.moneytrackerbeta.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        UserService userService = new UserServiceImpl();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String page;
        try {
            if (userService.verifyLogin(username, password)) {
                User user = userService.findUser(username);
                CurrencyDao currencyDao = new CurrencyDao();
                List<Currency> currencies = currencyDao.findAll();
                Currency userDefaultCurrency = currencyDao.findById(user.getDefaultCurrency());
                
                List<Asset> allAssets = userService.findAssets(user.getId());
                List<Asset> accounts = allAssets.stream().filter(Asset::isAccount).toList();
                List<Asset> incomes = allAssets.stream().filter(Asset::isIncome).toList();
                List<Asset> expenses = allAssets.stream().filter(Asset::isExpense).toList();
                List<TransactionType> types = List.of(TransactionType.getDisplayableTypes());

                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("accounts", accounts);
                session.setAttribute("incomeSources", incomes);
                session.setAttribute("expenseSources", expenses);
                session.setAttribute("transactionTypes", types);
                session.setAttribute("currencies", currencies);
                session.setAttribute("userDefaultCurrency", userDefaultCurrency);
                session.setAttribute("isLoggedIn", true);

                page = DashboardCommand.prepareDashboard(request);
            } else {
                request.setAttribute("errorUserPassMessage", "Invalid username or password");
                page = "pages/login.jsp";
            }
        } catch (ServiceException e) {
            request.setAttribute("errorMessage", "An error occurred while logging in. Try again later.");
            page = "pages/login.jsp";
        } catch (DaoException e) {
            logger.error("Error while retrieving user data", e);
            throw new CommandException("Error while retrieving user data", e);
        }
        return page;
    }
}
