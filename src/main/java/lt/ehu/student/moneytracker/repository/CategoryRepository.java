package lt.ehu.student.moneytracker.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import lt.ehu.student.moneytracker.model.Category;
import lt.ehu.student.moneytracker.model.User;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByUserAndType(User user, Category.CategoryType type);
    List<Category> findByUser(User user);
    boolean existsByNameAndUser(String name, User user);
    Optional<Category> findByIdAndUserId(UUID id, Integer userId);
    
    @Query(value = "SELECT DISTINCT c.* FROM categories c " +
           "INNER JOIN transactions t ON " +
           "(t.source = c.id AND t.source_type = 'Category') OR " +
           "(t.destination = c.id AND t.destination_type = 'Category') " +
           "WHERE c.user_id = :#{#user.id} AND c.type = :#{#type.name()}", 
           nativeQuery = true)
    List<Category> findUsedCategoriesByUserAndType(User user, Category.CategoryType type);
    
    void deleteByUserAndType(User user, Category.CategoryType type);
}