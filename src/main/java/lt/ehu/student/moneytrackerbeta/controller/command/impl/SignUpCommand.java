package lt.ehu.student.moneytrackerbeta.controller.command.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.http.HttpServletRequest;
import lt.ehu.student.moneytrackerbeta.controller.command.Command;
import lt.ehu.student.moneytrackerbeta.exception.CommandException;
import lt.ehu.student.moneytrackerbeta.exception.ServiceException;
import lt.ehu.student.moneytrackerbeta.service.UserService;
import lt.ehu.student.moneytrackerbeta.service.impl.UserServiceImpl;
import lt.ehu.student.moneytrackerbeta.utility.ValidationUtil;

public class SignUpCommand implements Command {
    private static final Logger logger = LogManager.getLogger(SignUpCommand.class.getName());
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        UserService userService = new UserServiceImpl();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        int defaultCurrency = Integer.parseInt(request.getParameter("default_curr"));
        username = ValidationUtil.sanitizeInput(username);
        password = ValidationUtil.sanitizeInput(password);
        confirmPassword = ValidationUtil.sanitizeInput(confirmPassword);
        firstName = ValidationUtil.sanitizeInput(firstName);
        lastName = ValidationUtil.sanitizeInput(lastName);
        email = ValidationUtil.sanitizeInput(email);
        if (!ValidationUtil.isValidInput(username) || !ValidationUtil.isValidInput(password) || !ValidationUtil.isValidInput(confirmPassword) || !ValidationUtil.isValidInput(firstName) || !ValidationUtil.isValidInput(lastName) || !ValidationUtil.isValidInput(email)) {
            logger.warn("Invalid input during registration provided by user: %s", username);
            request.setAttribute("errorMessage", "Invalid input. Verify your input and try again.");
            return "pages/signup.jsp";
        }
        try {
            if (userService.isUsernameTaken(username)) {
                logger.warn("Username already taken: %s", username);
                request.setAttribute("errorUserNameTaken", "Username is already taken. Try a different one.");
                return "pages/signup.jsp";
            }
        } catch (ServiceException e) {
            logger.error("An error occurred while checking username availability: %s", username);
            request.setAttribute("errorMessage", "An error occurred while checking username availability.");
            return "pages/signup.jsp";
        }
        if (!password.equals(confirmPassword)) {
            logger.warn("Password mismatch: %s", username);
            request.setAttribute("errorPasswordMismatch", "Passwords do not match.");
            return "pages/signup.jsp";
        }
        try {
            if (!userService.registerUser(username, password, firstName, lastName, defaultCurrency, email)) {
                logger.error("An error occurred while registering the user: %s", username);
                request.setAttribute("errorMessage", "An error occurred while registering the user.");
                return "pages/signup.jsp";
            };
        } catch (ServiceException e) {
            logger.error("An error occurred while registering the user: %s", username);
            request.setAttribute("errorMessage", "An error occurred while registering the user.");
            return "pages/signup.jsp";
        }
        request.setAttribute("firstName", firstName);
        logger.info("User registered successfully: %s", username);
        return "pages/signup_success.jsp";
    }
}
