package lt.ehu.student.moneytrackerbeta.controller.command.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lt.ehu.student.moneytrackerbeta.controller.command.Command;
import lt.ehu.student.moneytrackerbeta.exception.CommandException;
import lt.ehu.student.moneytrackerbeta.model.Asset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class NewTransactionCommand implements Command {
    private static final Logger logger = LogManager.getLogger(NewTransactionCommand.class);

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
//        int userId = (int) session.getAttribute("userId");
        String firstName = (String) session.getAttribute("firstName");
        List<Asset> accounts = (List<Asset>) session.getAttribute("accounts");
        List<Asset> incomes = (List<Asset>) session.getAttribute("incomeSources");
        List<Asset> expenses = (List<Asset>) session.getAttribute("expenseSources");
        boolean isLoggedIn = (boolean) session.getAttribute("isLoggedIn");
        if (!isLoggedIn) {
            return "pages/login.jsp";
        }
//        String type = request.getParameter("type");
//        String source = request.getParameter("source");
//        String destination = request.getParameter("destination");
//        String amount = request.getParameter("amount");
//        logger.debug("Adding transaction: user_id={}, type={}, source={}, destination={}, amount={}", userId, type, source, destination, amount);
//        // TODO: Validate input and add transaction to the database
//
//        request.setAttribute("successfulTransactionMsg", "Transaction added successfully!");
        return NewTransactionCommand.prepareTransactionForm(request, firstName, accounts, incomes, expenses);

    }

    public static String prepareTransactionForm(HttpServletRequest request, String firstName, List<Asset> accounts, List<Asset> incomes, List<Asset> expenses) {
        request.setAttribute("accounts", accounts);
        request.setAttribute("incomeSources", incomes);
        request.setAttribute("expenseSources", expenses);
        request.setAttribute("userName", firstName);
        return "pages/new_transaction.jsp";
    }
}