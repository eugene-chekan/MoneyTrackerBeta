package lt.ehu.student.moneytracker.controller;

import lt.ehu.student.moneytracker.model.Transaction;
import lt.ehu.student.moneytracker.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.annotation.Secured;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/transactions")
@Secured("USER")
public class TransactionController {
    private final TransactionService transactionService;
    private final UserService userService;
    private final AssetService assetService;
    private final TransactionTypeService transactionTypeService;
    private final CurrencyService currencyService;

    @Autowired
    public TransactionController(TransactionService transactionService,
                               UserService userService,
                               AssetService assetService,
                               TransactionTypeService transactionTypeService,
                               CurrencyService currencyService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.assetService = assetService;
        this.transactionTypeService = transactionTypeService;
        this.currencyService = currencyService;
    }

    @GetMapping
    public String listTransactions(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            Model model) {
        
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));

        if (from == null) {
            from = LocalDateTime.now().minusMonths(1);
        }
        if (to == null) {
            to = LocalDateTime.now();
        }

        model.addAttribute("transactions", 
            transactionService.findByUserIdAndDateRange(user.getId(), from, to));
        model.addAttribute("transactionTypes", transactionTypeService.findAll());
        model.addAttribute("assets", assetService.findByUserId(user.getId()));
        model.addAttribute("currencies", currencyService.findAll());
        
        return "transactions/list";
    }

    @GetMapping("/new")
    public String newTransactionForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
            
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("transactionTypes", transactionTypeService.findAll());
        model.addAttribute("assets", assetService.findByUserId(user.getId()));
        model.addAttribute("currencies", currencyService.findAll());
        
        return "transactions/form";
    }

    @PostMapping
    public String saveTransaction(@ModelAttribute Transaction transaction, 
                                @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
            
        transaction.setUserId(user.getId());
        transactionService.save(transaction);
        
        return "redirect:/transactions";
    }

    @PostMapping("/{id}/delete")
    public String deleteTransaction(@PathVariable UUID id) {
        transactionService.delete(id);
        return "redirect:/transactions";
    }
} 