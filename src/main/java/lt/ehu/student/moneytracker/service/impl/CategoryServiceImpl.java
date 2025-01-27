package lt.ehu.student.moneytracker.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt.ehu.student.moneytracker.exception.ResourceNotFoundException;
import lt.ehu.student.moneytracker.model.Category;
import lt.ehu.student.moneytracker.model.User;
import lt.ehu.student.moneytracker.repository.CategoryRepository;
import lt.ehu.student.moneytracker.service.CategoryService;
import lt.ehu.student.moneytracker.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category getById(UUID id) {
        return findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public List<Category> findByUserAndType(Integer userId, Category.CategoryType type) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return categoryRepository.findByUserAndType(user, type);
    }

    @Override
    public List<Category> findByUserId(Integer userId) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return categoryRepository.findByUser(user);
    }

    @Override
    public Category save(Category category) {
        if (category.getId() == null && 
            categoryRepository.existsByNameAndUser(category.getName(), category.getUser())) {
            throw new IllegalArgumentException("Category with this name already exists for this user");
        }
        return categoryRepository.save(category);
    }

    @Override
    public void delete(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public boolean existsByNameAndUser(String name, Integer userId) {
        User user = userService.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return categoryRepository.existsByNameAndUser(name, user);
    }

    @Override
    public Optional<Category> findByIdAndUserId(UUID id, Integer userId) {
        return categoryRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public boolean existsById(UUID id) {
        return categoryRepository.existsById(id);
    }
}