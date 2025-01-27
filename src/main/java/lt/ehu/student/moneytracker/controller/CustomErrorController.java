package lt.ehu.student.moneytracker.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        
        model.addAttribute("timestamp", new Date());
        model.addAttribute("path", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("status", statusCode);
            
            switch (statusCode) {
                case 404:
                    model.addAttribute("error", "Page Not Found");
                    model.addAttribute("message", "The page you're looking for doesn't exist.");
                    break;
                case 403:
                    model.addAttribute("error", "Access Denied");
                    model.addAttribute("message", "You don't have permission to access this resource.");
                    break;
                case 500:
                    model.addAttribute("error", "Internal Server Error");
                    model.addAttribute("message", "Something went wrong on our end. Please try again later.");
                    break;
                default:
                    model.addAttribute("error", "Error");
                    model.addAttribute("message", message != null ? message : "An unexpected error occurred.");
            }
        }
        
        // Add stack trace if available
        if (exception != null && exception instanceof Exception) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ((Exception) exception).printStackTrace(pw);
            model.addAttribute("trace", sw.toString());
        }
        
        return "error";
    }
} 