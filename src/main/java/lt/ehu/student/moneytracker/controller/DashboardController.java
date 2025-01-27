package lt.ehu.student.moneytracker.controller;

import lt.ehu.student.moneytracker.dto.TransactionDTO;
import lt.ehu.student.moneytracker.model.Asset;
import lt.ehu.student.moneytracker.model.Transaction;
import lt.ehu.student.moneytracker.model.User;
import lt.ehu.student.moneytracker.model.Category;
import lt.ehu.student.moneytracker.service.AssetService;
import lt.ehu.student.moneytracker.service.TransactionService;
import lt.ehu.student.moneytracker.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@Secured({"USER", "ADMIN"})
public class DashboardController {
    private final AssetService assetService;
    private final TransactionService transactionService;
    private final CategoryService categoryService;

    @Autowired
    public DashboardController(AssetService assetService, 
                             TransactionService transactionService,
                             CategoryService categoryService) {
        this.assetService = assetService;
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(@ModelAttribute("currentUser") User user, Model model) {
        List<Asset> assets = assetService.findByUserId(user.getId());
        List<Transaction> transactions = transactionService
            .findByUserIdAndDateRange(
                user.getId(),
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now()
            );
        
        // Prepare transaction data with resolved sources and destinations
        var transactionDTOs = transactions.stream()
            .map(transaction -> new TransactionDTO(
                transaction,
                resolveEntityName(transaction.getSourceId()),
                resolveEntityName(transaction.getDestinationId())
            ))
            .collect(Collectors.toList());

        List<Category> categories = categoryService.findByUserId(user.getId());

        model.addAttribute("assets", assets);
        model.addAttribute("recentTransactions", transactionDTOs);
        model.addAttribute("categories", categories);
        model.addAttribute("user", user);
        
        return "dashboard";
    }

    private String resolveEntityName(UUID id) {
        if (id == null) return "-";
        
        if (assetService.existsById(id)) {
            return assetService.getById(id).getName();
        }
        if (categoryService.existsById(id)) {
            return categoryService.getById(id).getName();
        }
        return "-";
    }
} 