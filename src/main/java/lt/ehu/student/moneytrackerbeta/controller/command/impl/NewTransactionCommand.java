package lt.ehu.student.moneytrackerbeta.controller.command.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lt.ehu.student.moneytrackerbeta.controller.command.Command;
import lt.ehu.student.moneytrackerbeta.exception.CommandException;
import lt.ehu.student.moneytrackerbeta.model.Asset;
import lt.ehu.student.moneytrackerbeta.model.TransactionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.util.List;

public class NewTransactionCommand implements Command {
    private static final Logger logger = LogManager.getLogger(NewTransactionCommand.class);

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        String firstName = (String) session.getAttribute("firstName");
        List<Asset> accounts = (List<Asset>) session.getAttribute("accounts");
        List<Asset> incomes = (List<Asset>) session.getAttribute("incomeSources");
        List<Asset> expenses = (List<Asset>) session.getAttribute("expenseSources");
        TransactionType[] types = TransactionType.getDisplayableTypes();
        boolean isLoggedIn = (boolean) session.getAttribute("isLoggedIn");
        if (!isLoggedIn) {
            return "pages/login.jsp";
        }
        return NewTransactionCommand.prepareTransactionForm(request, firstName, accounts, incomes, expenses, types);

    }

    public static String prepareTransactionForm(HttpServletRequest request, String firstName, List<Asset> accounts, List<Asset> incomes, List<Asset> expenses, TransactionType[] types) {
        request.setAttribute("accounts", accounts);
        request.setAttribute("incomeSources", incomes);
        request.setAttribute("expenseSources", expenses);
        request.setAttribute("transactionTypes", types);
        request.setAttribute("now", new Timestamp(System.currentTimeMillis()));
        request.setAttribute("userName", firstName);
        return "pages/new_transaction.jsp";
    }
}