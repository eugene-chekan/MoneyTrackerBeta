package lt.ehu.student.moneytracker.service;

import java.util.List;
import java.util.Optional;

import lt.ehu.student.moneytracker.model.Category;

public interface CategoryService {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Category getById(Long id);  // New method that throws exception if not found
    List<Category> findByUserAndType(Integer userId, Category.CategoryType type);
    List<Category> findByUserId(Integer userId);
    Optional<Category> findByIdAndUserId(Long id, Integer userId);
    Category save(Category category);
    void delete(Long id);
    boolean existsByNameAndUser(String name, Integer userId);
} 