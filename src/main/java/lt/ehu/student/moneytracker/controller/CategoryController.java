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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lt.ehu.student.moneytracker.model.User;

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
            @ModelAttribute("currentUser") User user,
            @RequestParam(required = false) String type,
            Model model) {
        
        Category.CategoryType categoryType = type != null ? 
            Category.CategoryType.valueOf(type.toUpperCase()) : 
            Category.CategoryType.EXPENSE;
            
        var categories = categoryService.findByUserAndType(user.getId(), categoryType);
        
        model.addAttribute("categories", categories);
        model.addAttribute("selectedType", categoryType);
        model.addAttribute("types", Category.CategoryType.values());
        
        return "categories/list";
    }

    @GetMapping("/new")
    public String newCategoryForm(Model model, @RequestParam(required = false) String type) {
        model.addAttribute("category", Category.builder()
            .type(type != null ? Category.CategoryType.valueOf(type) : null)
            .build());
        model.addAttribute("types", Category.CategoryType.values());
        return "categories/form";
    }

    @PostMapping
    public String createCategory(@ModelAttribute Category category,
                            @ModelAttribute("currentUser") User user,
                            RedirectAttributes redirectAttributes) {
            
        category.setUser(user);
        categoryService.save(category);
        
        redirectAttributes.addFlashAttribute("success", "Category was successfully created!");
        return "redirect:/categories?type=" + category.getType();
    }

    @GetMapping("/{id}/edit")
    public String editCategoryForm(@PathVariable Long id, 
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
        
        var category = categoryService.findByIdAndUserId(id, user.getId())
            .orElseThrow(() -> new IllegalStateException("Category not found"));
        
        model.addAttribute("category", category);
        model.addAttribute("types", Category.CategoryType.values());
        return "categories/form";
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id,
                            @AuthenticationPrincipal UserDetails userDetails) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
            
        if (!categoryService.findByIdAndUserId(id, user.getId()).isPresent()) {
            throw new IllegalStateException("Category not found");
        }
            
        categoryService.delete(id);
        return "redirect:/categories";
    }

    @PostMapping("/{id}/edit")
    public String updateCategory(@PathVariable Long id,
                               @ModelAttribute Category category,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        var user = userService.findByLogin(userDetails.getUsername())
            .orElseThrow(() -> new IllegalStateException("User not found"));
        
        var existingCategory = categoryService.findByIdAndUserId(id, user.getId())
            .orElseThrow(() -> new IllegalStateException("Category not found"));
        
        existingCategory.setName(category.getName());
        existingCategory.setEmojiIcon(category.getEmojiIcon());
        existingCategory.setType(category.getType());
        
        categoryService.save(existingCategory);
        
        redirectAttributes.addFlashAttribute("success", "Category was successfully updated!");
        return "redirect:/categories?type=" + existingCategory.getType();
    }
} 