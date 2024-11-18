package lt.ehu.student.moneytrackerbeta.controller.command.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lt.ehu.student.moneytrackerbeta.controller.command.Command;
import lt.ehu.student.moneytrackerbeta.exception.CommandException;
import lt.ehu.student.moneytrackerbeta.exception.ServiceException;
import lt.ehu.student.moneytrackerbeta.model.User;
import lt.ehu.student.moneytrackerbeta.service.TransactionService;
import lt.ehu.student.moneytrackerbeta.service.impl.TransactionServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SubmitTransactionCommand implements Command {
    private static final Logger logger = LogManager.getLogger(SubmitTransactionCommand.class);

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int type = Integer.parseInt(request.getParameter("type"));
        String source = request.getParameter("source");
        String destination = request.getParameter("destination");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(request.getParameter("amount")));
        String dateStr = request.getParameter("transactionDate");
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.parse(dateStr), LocalTime.now());
        Timestamp date = Timestamp.valueOf(localDateTime);
        String comment = request.getParameter("comment");
        logger.debug("Adding transaction: user_id={}, type={}, source={}, destination={}, amount={}, date={}, comment={}", user.getId(), type, source, destination, amount, date, comment);
        TransactionService transactionService = new TransactionServiceImpl();
        try {
            boolean success = transactionService.addTransaction(user.getId(), type, source, destination, date, amount, comment);
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
