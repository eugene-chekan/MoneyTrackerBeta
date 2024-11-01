package lt.ehu.student.moneytrackerbeta.controller.command.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lt.ehu.student.moneytrackerbeta.controller.command.Command;
import lt.ehu.student.moneytrackerbeta.model.Asset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

public class DashboardCommand implements Command {
    private static final Logger logger = LogManager.getLogger(DashboardCommand.class);
    @Override
    public String execute(HttpServletRequest request) {
        // Get user ID from session instead of login parameters
        String firstName = (String) request.getSession().getAttribute("firstName");
        List<Asset> accounts = (List<Asset>) request.getSession().getAttribute("accounts");
        List<Asset> incomes = (List<Asset>) request.getSession().getAttribute("incomeSources");
        List<Asset> expenses = (List<Asset>) request.getSession().getAttribute("expenseSources");

        return prepareDashboard(request, firstName, accounts, incomes, expenses);
    }

    public static String prepareDashboard(HttpServletRequest request, String firstName, List<Asset> accounts, List<Asset> incomes, List<Asset> expenses) {
        HttpSession session = request.getSession();
        boolean isLoggedIn = (boolean)session.getAttribute("isLoggedIn");
        logger.debug("Is user logged in: {}", isLoggedIn);
        if  (! isLoggedIn) {
            return "pages/login.jsp";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd yyyy");
        String date = dateFormat.format(new java.util.Date());


        BigDecimal income = new BigDecimal("100.99"); // TODO: add income calculation logic
        BigDecimal expense = new BigDecimal("51.23"); // TODO: add expense calculation logic

        request.setAttribute("accounts", accounts);
        request.setAttribute("incomeSources", incomes);
        request.setAttribute("expenseSources", expenses);
        request.setAttribute("userName", firstName);
        request.setAttribute("currentDate", date);
        request.setAttribute("income", income);
        request.setAttribute("expense", expense);
        request.setAttribute("balance", income.subtract(expense));

        return "pages/dashboard.jsp";
    }
}

