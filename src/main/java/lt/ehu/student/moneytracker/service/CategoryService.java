package lt.ehu.student.moneytracker.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lt.ehu.student.moneytracker.model.Category;

public interface CategoryService {
    List<Category> findAll();
    Optional<Category> findById(UUID id);
    Category getById(UUID id);  // New method that throws exception if not found
    List<Category> findByUserAndType(Integer userId, Category.CategoryType type);
    List<Category> findByUserId(Integer userId);
    Optional<Category> findByIdAndUserId(UUID id, Integer userId);
    Category save(Category category);
    void delete(UUID id);
    boolean existsByNameAndUser(String name, Integer userId);
    boolean existsById(UUID id);
} 