package lt.ehu.student.moneytrackerbeta.controller.command.impl;

import jakarta.servlet.http.HttpServletRequest;
import lt.ehu.student.moneytrackerbeta.controller.command.Command;
import lt.ehu.student.moneytrackerbeta.exception.CommandException;
import lt.ehu.student.moneytrackerbeta.exception.ServiceException;
import lt.ehu.student.moneytrackerbeta.model.User;
import lt.ehu.student.moneytrackerbeta.model.dto.TransactionDto;
import lt.ehu.student.moneytrackerbeta.service.impl.TransactionServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ViewTransactionsCommand implements Command {
    private static final Logger logger = LogManager.getLogger(ViewTransactionsCommand.class);

    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        TransactionServiceImpl transactionService = new TransactionServiceImpl();
        User user = (User) request.getSession().getAttribute("user");
        int type = Integer.parseInt(request.getParameter("type"));
        String fromDateStr = request.getParameter("dateFrom");
        String toDateStr = request.getParameter("dateTo");
        
        // Start of the first day
        LocalDateTime fromDateTime = LocalDateTime.of(LocalDate.parse(fromDateStr), LocalTime.MIDNIGHT);
        // End of the last day (23:59:59.999999999)
        LocalDateTime toDateTime = LocalDateTime.of(LocalDate.parse(toDateStr), LocalTime.MAX);
        
        Timestamp fromDate = Timestamp.valueOf(fromDateTime);
        Timestamp toDate = Timestamp.valueOf(toDateTime);
        
        try {
            List<TransactionDto> transactions = transactionService.findFilteredTransactions(user.getId(), type, fromDate, toDate);
            request.setAttribute("transactions", transactions);
        } catch (ServiceException e) {
            logger.error("Error while retrieving transactions", e);
            throw new CommandException("Error while retrieving transactions", e);
        }
        return "pages/view_transactions.jsp";
    }

    @Override
    public void refresh() {
        Command.super.refresh();
    }
}
