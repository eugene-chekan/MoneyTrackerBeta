package lt.ehu.student.moneytracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import lt.ehu.student.moneytracker.service.CategoryService;
import lt.ehu.student.moneytracker.service.UserService;
import lt.ehu.student.moneytracker.model.Category;

@Controller
@RequestMapping("/categories")
@Secured({"USER", "ADMIN"})
public class CategoryController {
    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping
    public String listCategories(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String type,
            Model model) {
        
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
            
        Category.CategoryType categoryType = type != null ? 
            Category.CategoryType.valueOf(type.toUpperCase()) : 
            Category.CategoryType.EXPENSE;
            
        var categories = categoryService.findByUserAndType(user.getId(), categoryType);
        
        model.addAttribute("categories", categories);
        model.addAttribute("selectedType", categoryType);
        model.addAttribute("types", Category.CategoryType.values());
        
        return "categories/list";
    }
} 