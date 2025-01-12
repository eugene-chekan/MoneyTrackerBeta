package lt.ehu.student.moneytracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lt.ehu.student.moneytracker.service.RoleService;
import lt.ehu.student.moneytracker.service.UserService;
import lt.ehu.student.moneytracker.service.CurrencyService;
import lt.ehu.student.moneytracker.service.AssetService;

@Controller
@RequestMapping("/admin")
@Secured("ADMIN")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final CurrencyService currencyService;
    private final AssetService assetService;

    @Autowired
    public AdminController(UserService userService, 
                         RoleService roleService, 
                         CurrencyService currencyService, 
                         AssetService assetService) {
        this.userService = userService;
        this.roleService = roleService;
        this.currencyService = currencyService;
        this.assetService = assetService;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.findAll());
        return "admin/users/list";
    }

    @PostMapping("/users/{userId}/role")
    public String updateUserRole(@PathVariable Integer userId,
                               @RequestParam String roleName) {
        userService.updateUserRole(userId, roleName);
        return "redirect:/admin/users";
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("userCount", userService.count());
        model.addAttribute("currencyCount", currencyService.count());
        model.addAttribute("assetCount", assetService.count());
        // model.addAttribute("transactionCount", transactionService.count());
        
        return "admin/dashboard";
    }
} 