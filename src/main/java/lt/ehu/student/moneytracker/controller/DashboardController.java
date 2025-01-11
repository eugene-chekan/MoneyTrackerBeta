package lt.ehu.student.moneytracker.controller;

import lt.ehu.student.moneytracker.model.Asset;
import lt.ehu.student.moneytracker.model.Transaction;
import lt.ehu.student.moneytracker.service.AssetService;
import lt.ehu.student.moneytracker.service.TransactionService;
import lt.ehu.student.moneytracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.annotation.Secured;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@Secured("USER")
public class DashboardController {
    private final AssetService assetService;
    private final TransactionService transactionService;
    private final UserService userService;

    @Autowired
    public DashboardController(AssetService assetService, 
                             TransactionService transactionService,
                             UserService userService) {
        this.assetService = assetService;
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
        
        List<Asset> assets = assetService.findByUserId(user.getId());
        List<Transaction> recentTransactions = transactionService
            .findByUserIdAndDateRange(
                user.getId(),
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now()
            );

        model.addAttribute("assets", assets);
        model.addAttribute("recentTransactions", recentTransactions);
        model.addAttribute("user", user);
        
        return "dashboard";
    }
} 