package lt.ehu.student.moneytracker.controller;

import lt.ehu.student.moneytracker.dto.SimpleItemDto;
import lt.ehu.student.moneytracker.model.Category.CategoryType;
import lt.ehu.student.moneytracker.model.Transaction;
import lt.ehu.student.moneytracker.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.annotation.Secured;
import java.util.UUID;

@Controller
@RequestMapping("/transactions")
@Secured({"USER", "ADMIN"})
public class TransactionController {
    private final TransactionService transactionService;
    private final UserService userService;
    private final AssetService assetService;
    private final CategoryService categoryService;
    private final CurrencyService currencyService;
    private final TransactionTypeService transactionTypeService;

    @Autowired
    public TransactionController(TransactionService transactionService,
                               UserService userService,
                               AssetService assetService,
                               CategoryService categoryService,
                               CurrencyService currencyService,
                               TransactionTypeService transactionTypeService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.assetService = assetService;
        this.categoryService = categoryService;
        this.currencyService = currencyService;
        this.transactionTypeService = transactionTypeService;
    }

    @GetMapping
    public String listTransactions(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
        
        model.addAttribute("transactions", transactionService.findByUserId(user.getId()));
        return "transactions/list";
    }

    @GetMapping("/new")
    public String newTransactionForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
        
        // Convert categories and assets to simple DTOs
        var allCategories = categoryService.findByUserId(user.getId());
        var incomeCategories = allCategories.stream()
            .filter(c -> c.getType() == CategoryType.INCOME)
            .map(c -> new SimpleItemDto(c.getId(), c.getName(), c.getEmojiIcon()))
            .toList();
        var expenseCategories = allCategories.stream()
            .filter(c -> c.getType() == CategoryType.EXPENSE)
            .map(c -> new SimpleItemDto(c.getId(), c.getName(), c.getEmojiIcon()))
            .toList();
        var assets = assetService.findByUserId(user.getId()).stream()
            .map(a -> new SimpleItemDto(a.getId(), a.getName()))
            .toList();
        
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("transactionTypes", transactionTypeService.findAll());
        model.addAttribute("incomeCategories", incomeCategories);
        model.addAttribute("expenseCategories", expenseCategories);
        model.addAttribute("assets", assets);
        
        return "transactions/form";
    }

    @PostMapping
    public String saveTransaction(@ModelAttribute Transaction transaction,
                                @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
            
        transaction.setUser(user);
        transactionService.save(transaction);
        
        return "redirect:/transactions";
    }

    @GetMapping("/{id}/edit")
    public String editTransactionForm(@PathVariable UUID id,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    Model model) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
            
        var transaction = transactionService.findByIdAndUserId(id, user.getId())
            .orElseThrow(() -> new IllegalStateException("Transaction not found"));
        
        model.addAttribute("transaction", transaction);
        model.addAttribute("categories", categoryService.findByUserId(user.getId()));
        model.addAttribute("transactionTypes", transactionTypeService.findAll());
        model.addAttribute("assets", assetService.findByUserId(user.getId()));
        model.addAttribute("currencies", currencyService.findAll());
        
        return "transactions/form";
    }

    @PostMapping("/{id}/delete")
    public String deleteTransaction(@PathVariable UUID id,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
            
        transactionService.validateUserAccess(id, user.getId());
        transactionService.delete(id);
        
        return "redirect:/transactions";
    }
} 