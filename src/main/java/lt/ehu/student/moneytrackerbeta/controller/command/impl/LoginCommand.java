package lt.ehu.student.moneytrackerbeta.controller.command.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lt.ehu.student.moneytrackerbeta.controller.command.Command;
import lt.ehu.student.moneytrackerbeta.exception.ServiceException;
import lt.ehu.student.moneytrackerbeta.model.Asset;
import lt.ehu.student.moneytrackerbeta.service.UserService;
import lt.ehu.student.moneytrackerbeta.service.impl.UserServiceImpl;

import java.util.List;

public class LoginCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        UserService userService = new UserServiceImpl();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String page;
        try {
            if (userService.verifyLogin(username, password)) {
                String firstName = userService.getFirstName(username);
                int userId = userService.getUserId(username);
                List<Asset> allAssets = userService.findAssets(userId);
                List<Asset> accounts = allAssets.stream().filter(Asset::isAccount).toList();
                List<Asset> incomes = allAssets.stream().filter(Asset::isIncome).toList();
                List<Asset> expenses = allAssets.stream().filter(Asset::isExpense).toList();

                HttpSession session = request.getSession();
                session.setAttribute("accounts", accounts);
                session.setAttribute("incomeSources", incomes);
                session.setAttribute("expenseSources", expenses);
                session.setAttribute("userId", userId);
                session.setAttribute("userName", firstName);
                session.setAttribute("isLoggedIn", true);

                page = DashboardCommand.prepareDashboard(request, firstName, accounts, incomes, expenses);
            } else {
                request.setAttribute("errorUserPassMessage", "Invalid username or password");
                page = "pages/login.jsp";
            }
        } catch (ServiceException e) {
            request.setAttribute("errorMessage", "An error occurred while logging in. Try again later.");
            page = "pages/login.jsp";
        }
        return page;
    }
}
