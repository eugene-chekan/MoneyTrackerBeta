package lt.ehu.student.moneytracker.controller;

import lt.ehu.student.moneytracker.model.User;
import lt.ehu.student.moneytracker.service.UserService;
import lt.ehu.student.moneytracker.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.access.annotation.Secured;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final CurrencyService currencyService;

    @Autowired
    public AuthController(UserService userService, CurrencyService currencyService) {
        this.userService = userService;
        this.currencyService = currencyService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("currencies", currencyService.findAll());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, RedirectAttributes redirectAttributes, Model model) {
        // Check if username exists
        if (userService.existsByLogin(user.getLogin())) {
            model.addAttribute("error", "Username is already taken. Please choose another one.");
            model.addAttribute("currencies", currencyService.findAll());
            return "auth/register";
        }

        // Check if email exists
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email is already registered. Please use another email.");
            model.addAttribute("currencies", currencyService.findAll());
            return "auth/register";
        }

        try {
            userService.register(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Please try again.");
            model.addAttribute("currencies", currencyService.findAll());
            return "auth/register";
        }
    }

    @PostMapping("/logout")
    @Secured("USER")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/auth/login";
    }
} 