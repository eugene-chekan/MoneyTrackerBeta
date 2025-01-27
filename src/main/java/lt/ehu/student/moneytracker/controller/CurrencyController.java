package lt.ehu.student.moneytracker.controller;

import lt.ehu.student.moneytracker.model.Currency;
import lt.ehu.student.moneytracker.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/currencies")
@Secured("ADMIN")
public class CurrencyController {
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public String listCurrencies(Model model) {
        model.addAttribute("currencies", currencyService.findAll());
        return "admin/currencies/list";
    }

    @GetMapping("/new")
    public String newCurrencyForm(Model model) {
        model.addAttribute("currency", new Currency());
        return "admin/currencies/form";
    }

    @PostMapping
    public String saveCurrency(@ModelAttribute Currency currency) {
        currencyService.save(currency);
        return "redirect:/admin/currencies";
    }

    @GetMapping("/{id}/edit")
    public String editCurrencyForm(@PathVariable Integer id, Model model) {
        var currency = currencyService.findById(id)
            .orElseThrow(() -> new IllegalStateException("Currency not found"));
        model.addAttribute("currency", currency);
        return "admin/currencies/form";
    }

    @PostMapping("/{id}/delete")
    public String deleteCurrency(@PathVariable Integer id) {
        currencyService.delete(id);
        return "redirect:/admin/currencies";
    }
} 