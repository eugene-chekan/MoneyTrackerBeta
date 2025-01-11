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
@Secured("ADMIN")  // Only admins can access
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final AssetService assetService;
    private final CurrencyService currencyService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, CurrencyService currencyService, AssetService assetService) {
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

    @PostMapping("/users/{userId}/roles")
    public String updateUserRoles(@PathVariable Integer userId,
                                @RequestParam String roleName,
                                @RequestParam boolean add) {
        if (add) {
            userService.addRoleToUser(userId, roleName);
        } else {
            userService.removeRoleFromUser(userId, roleName);
        }
        return "redirect:/admin/users";
    }

    @GetMapping
    public String dashboard(Model model) {
        // Add basic statistics
        model.addAttribute("userCount", userService.count());
        model.addAttribute("currencyCount", currencyService.count());
        // Add these if you have the corresponding services
        model.addAttribute("assetCount", assetService.count());
        // model.addAttribute("transactionCount", transactionService.count());
        
        return "admin/dashboard";
    }
} 