package lt.ehu.student.moneytracker.controller.advice;

import lt.ehu.student.moneytracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserControllerAdvice {
    
    private final UserService userService;

    @Autowired
    public UserControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("currentUser")
    public Object getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        return userService.findByLogin(authentication.getName()).orElse(null);
    }
} 