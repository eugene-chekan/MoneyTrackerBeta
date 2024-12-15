package lt.ehu.student.moneytrackerbeta.controller.command.impl;

import jakarta.servlet.http.HttpServletRequest;
import lt.ehu.student.moneytrackerbeta.controller.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogoutCommand implements Command {
    private static final Logger logger = LogManager.getLogger(LogoutCommand.class.getName());

    @Override
    public String execute(HttpServletRequest request) {
        logger.info("Logout command executed");
        request.getSession().invalidate();
        return "index.jsp";
    }
}
