package lt.ehu.student.moneytracker.controller;

import lt.ehu.student.moneytracker.dto.TransactionDTO;
import lt.ehu.student.moneytracker.model.Category.CategoryType;
import lt.ehu.student.moneytracker.model.Transaction;
import lt.ehu.student.moneytracker.model.User;
import lt.ehu.student.moneytracker.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.annotation.Secured;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.Hibernate;

@Controller
@RequestMapping("/transactions")
@Secured({"USER", "ADMIN"})
public class TransactionController {
    private final TransactionService transactionService;
    private final UserService userService;
    private final AssetService assetService;
    private final CategoryService categoryService;

    @Autowired
    public TransactionController(TransactionService transactionService,
                               UserService userService,
                               AssetService assetService,
                               CategoryService categoryService,
                               CurrencyService currencyService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.assetService = assetService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listTransactions(@RequestParam(required = false) String type,
                                 @ModelAttribute("currentUser") User user,
                                 Model model) {
        List<Transaction> transactions = transactionService.findByUserId(user.getId());
        
        // Convert transactions to DTOs
        List<TransactionDTO> transactionDTOs = transactions.stream()
            .map(transaction -> new TransactionDTO(
                transaction,
                resolveEntityName(transaction.getSourceId()),
                resolveEntityName(transaction.getDestinationId())
            ))
            .collect(Collectors.toList());

        model.addAttribute("transactions", transactionDTOs);
        model.addAttribute("selectedType", type);
        
        return "transactions/list";
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

    @GetMapping("/new")
    public String showTransactionForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));

        model.addAttribute("transaction", new Transaction());
        model.addAttribute("incomeCategories", categoryService.findByUserAndType(user.getId(), CategoryType.INCOME));
        model.addAttribute("expenseCategories", categoryService.findByUserAndType(user.getId(), CategoryType.EXPENSE));
        model.addAttribute("assets", assetService.findByUserId(user.getId()));
        return "transactions/form";
    }

    @PostMapping
    public String saveTransaction(@ModelAttribute Transaction transaction,
                                @RequestParam("type") String type,
                                @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
            
        transaction.setUser(user);
        
        if (type.equals("TRANSFER")) {
            // Get source asset's currency
            var sourceAsset = assetService.getById(transaction.getSourceId());
            var currency = sourceAsset.getCurrency();
            
            // Create expense transaction (from source only)
            Transaction expenseTransaction = Transaction.builder()
                .user(user)
                .sourceId(transaction.getSourceId())
                .amount(transaction.getAmount().negate())
                .currency(currency)
                .comment(transaction.getComment())
                .timestamp(transaction.getTimestamp())
                .build();
                
            // Create income transaction (to destination only)
            Transaction incomeTransaction = Transaction.builder()
                .user(user)
                .destinationId(transaction.getDestinationId())
                .amount(transaction.getAmount())
                .currency(currency)
                .comment(transaction.getComment())
                .timestamp(transaction.getTimestamp())
                .build();
                
            // Save both transactions and link them together
            expenseTransaction = transactionService.save(expenseTransaction);
            incomeTransaction = transactionService.save(incomeTransaction);
            
            expenseTransaction.setTransferTransactionId(incomeTransaction.getId());
            incomeTransaction.setTransferTransactionId(expenseTransaction.getId());
            
            transactionService.save(expenseTransaction);
            transactionService.save(incomeTransaction);
        } else {
            // Get currency from the asset
            if (type.equals("EXPENSE")) {
                var asset = assetService.getById(transaction.getSourceId());
                transaction.setCurrency(asset.getCurrency());
                if (transaction.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                    transaction.setAmount(transaction.getAmount().negate());
                }
            } else if (type.equals("INCOME")) {
                var asset = assetService.getById(transaction.getDestinationId());
                transaction.setCurrency(asset.getCurrency());
                if (transaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                    transaction.setAmount(transaction.getAmount().negate());
                }
            }
            transactionService.save(transaction);
        }
        
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
        
        // If this is a transfer, get both parts
        Transaction linkedTransaction = null;
        if (transaction.getTransferTransactionId() != null) {
            linkedTransaction = transactionService.getById(transaction.getTransferTransactionId());
        }
        
        var transactionDTO = new TransactionDTO(transaction, 
            resolveEntityName(transaction.getSourceId()),
            resolveEntityName(transaction.getDestinationId()));
        
        model.addAttribute("transaction", transactionDTO);
        if (linkedTransaction != null) {
            var linkedDTO = new TransactionDTO(linkedTransaction,
                resolveEntityName(linkedTransaction.getSourceId()),
                resolveEntityName(linkedTransaction.getDestinationId()));
            model.addAttribute("linkedTransaction", linkedDTO);
        }
        
        model.addAttribute("incomeCategories", categoryService.findByUserAndType(user.getId(), CategoryType.INCOME));
        model.addAttribute("expenseCategories", categoryService.findByUserAndType(user.getId(), CategoryType.EXPENSE));
        model.addAttribute("assets", assetService.findByUserId(user.getId()));
        
        return "transactions/form";
    }

    @PostMapping("/{id}/edit")
    public String updateTransaction(@PathVariable UUID id,
                                  @ModelAttribute Transaction transaction,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
            
        var existingTransaction = transactionService.findByIdAndUserId(id, user.getId())
            .orElseThrow(() -> new IllegalStateException("Transaction not found"));
        
        // Check if it's a transfer transaction
        if (existingTransaction.getTransferTransactionId() != null) {
            var linkedTransaction = transactionService.getById(existingTransaction.getTransferTransactionId());
            
            // Update both transactions
            if (existingTransaction.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                // This is the expense part of transfer
                existingTransaction.setAmount(transaction.getAmount().negate());
                linkedTransaction.setAmount(transaction.getAmount());
            } else {
                // This is the income part of transfer
                existingTransaction.setAmount(transaction.getAmount());
                linkedTransaction.setAmount(transaction.getAmount().negate());
            }
            
            existingTransaction.setComment(transaction.getComment());
            existingTransaction.setTimestamp(transaction.getTimestamp());
            linkedTransaction.setComment(transaction.getComment());
            linkedTransaction.setTimestamp(transaction.getTimestamp());
            
            transactionService.save(existingTransaction);
            transactionService.save(linkedTransaction);
        } else {
            // Regular transaction update
            existingTransaction.setAmount(transaction.getAmount());
            existingTransaction.setComment(transaction.getComment());
            existingTransaction.setTimestamp(transaction.getTimestamp());
            existingTransaction.setSourceId(transaction.getSourceId());
            existingTransaction.setDestinationId(transaction.getDestinationId());
            
            transactionService.save(existingTransaction);
        }
        
        return "redirect:/transactions";
    }

    @PostMapping("/{id}/delete")
    public String deleteTransaction(@PathVariable UUID id,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
            
        var transaction = transactionService.findByIdAndUserId(id, user.getId())
            .orElseThrow(() -> new IllegalStateException("Transaction not found"));
        
        // If this is a transfer transaction, delete both parts
        if (transaction.getTransferTransactionId() != null) {
            // Delete the linked transaction first
            transactionService.delete(transaction.getTransferTransactionId());
        }
        
        // Delete the main transaction
        transactionService.delete(id);
        
        return "redirect:/transactions";
    }
} 