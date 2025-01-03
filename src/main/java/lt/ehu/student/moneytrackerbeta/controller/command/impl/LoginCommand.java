package lt.ehu.student.moneytrackerbeta.controller.command.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lt.ehu.student.moneytrackerbeta.constant.PagePath;
import lt.ehu.student.moneytrackerbeta.controller.command.Command;
import lt.ehu.student.moneytrackerbeta.exception.ServiceException;
import lt.ehu.student.moneytrackerbeta.model.Asset;
import lt.ehu.student.moneytrackerbeta.model.Currency;
import lt.ehu.student.moneytrackerbeta.model.TransactionType;
import lt.ehu.student.moneytrackerbeta.model.User;
import lt.ehu.student.moneytrackerbeta.service.UserService;
import lt.ehu.student.moneytrackerbeta.service.impl.UserServiceImpl;
import lt.ehu.student.moneytrackerbeta.utility.ValidationUtil;
import lt.ehu.student.moneytrackerbeta.dao.impl.CurrencyDao;
import lt.ehu.student.moneytrackerbeta.dao.impl.TransactionTypeDao;
import lt.ehu.student.moneytrackerbeta.exception.CommandException;
import lt.ehu.student.moneytrackerbeta.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger(LoginCommand.class.getName());

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        UserService userService = new UserServiceImpl();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        username = ValidationUtil.sanitizeInput(username);
        password = ValidationUtil.sanitizeInput(password);
        if (!ValidationUtil.isValidInput(username) || !ValidationUtil.isValidInput(password)) {
            logger.warn("Invalid input during login provided by user: %s", username);
            request.setAttribute("errorMessage", "Invalid input. Verify your input and try again.");
            return PagePath.LOGIN;
        }
        String page;
        try {
            if (userService.verifyLogin(username, password)) {
                User user = userService.findUser(username);
                CurrencyDao currencyDao = new CurrencyDao();
                TransactionTypeDao transactionTypeDao = new TransactionTypeDao();
                List<Currency> currencies = currencyDao.findAll();
                Currency userDefaultCurrency = currencyDao.findById(user.getDefaultCurrency());
                
                List<Asset> allAssets = userService.findAssets(user.getId());
                List<TransactionType> types = transactionTypeDao.findAll();
                TransactionType accountType = types.stream().filter(type -> type.getName().equals(TransactionType.ACCOUNT)).findFirst().orElse(null);
                TransactionType incomeType = types.stream().filter(type -> type.getName().equals(TransactionType.INCOME)).findFirst().orElse(null);
                TransactionType expenseType = types.stream().filter(type -> type.getName().equals(TransactionType.EXPENSE)).findFirst().orElse(null);
                List<Asset> accounts = allAssets.stream().filter(asset -> asset.getTypeId() == accountType.getId()).toList();
                List<Asset> incomes = allAssets.stream().filter(asset -> asset.getTypeId() == incomeType.getId()).toList();
                List<Asset> expenses = allAssets.stream().filter(asset -> asset.getTypeId() == expenseType.getId()).toList();

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
                logger.warn("Invalid username or password provided by user: {}", username);
                request.setAttribute("errorUserPassMessage", "Invalid username or password");
                page = PagePath.LOGIN;
            }
        } catch (ServiceException e) {
            logger.error("An error occurred while logging in: {}", username);
            request.setAttribute("errorMessage", "An error occurred while logging in. Try again later.");
            page = PagePath.LOGIN;
        } catch (DaoException e) {
            logger.error("Error while retrieving user data", e);
            throw new CommandException("Error while retrieving user data", e);
        }
        return page;
    }
}
