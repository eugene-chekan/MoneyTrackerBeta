package lt.ehu.student.moneytrackerbeta.controller.command.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lt.ehu.student.moneytrackerbeta.controller.command.Command;
import lt.ehu.student.moneytrackerbeta.exception.CommandException;
import lt.ehu.student.moneytrackerbeta.exception.ServiceException;
import lt.ehu.student.moneytrackerbeta.service.TransactionService;
import lt.ehu.student.moneytrackerbeta.service.impl.TransactionServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class SubmitTransactionCommand implements Command {
    private static final Logger logger = LogManager.getLogger(SubmitTransactionCommand.class);

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String destination = request.getParameter("destination");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(request.getParameter("amount")));
        String date = request.getParameter("date"); // TODO: add appropriate field in the jsp page
        String comment = request.getParameter("comment");
        String currency = request.getParameter("currency");
        logger.debug("Adding transaction: user_id={}, type={}, source={}, destination={}, amount={}, date={}, comment={}, currency={}", userId, type, source, destination, amount, date, comment, currency);
        TransactionService transactionService = new TransactionServiceImpl();
        try {
            boolean success = transactionService.addTransaction(userId, type, source, destination, null, date, amount, comment, currency);
            if (success) {
                request.setAttribute("successfulTransactionMsg", "Transaction added successfully!");
            }
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return DashboardCommand.prepareDashboard(request);
    }

    @Override
    public void refresh() {
        Command.super.refresh();
    }
}
