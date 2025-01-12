package lt.ehu.student.moneytracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import lt.ehu.student.moneytracker.model.Category;
import lt.ehu.student.moneytracker.model.User;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserAndType(User user, Category.CategoryType type);
    List<Category> findByUser(User user);
    boolean existsByNameAndUser(String name, User user);
    Optional<Category> findByIdAndUser(Long id, User user);
    
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.transactions " +
           "WHERE c.user = :user AND c.type = :type")
    List<Category> findByUserAndTypeWithTransactions(User user, Category.CategoryType type);
    
    @Query("SELECT c FROM Category c " +
           "WHERE c.user = :user AND c.type = :type " +
           "AND EXISTS (SELECT t FROM Transaction t WHERE t.category = c)")
    List<Category> findUsedCategoriesByUserAndType(User user, Category.CategoryType type);
    
    void deleteByUserAndType(User user, Category.CategoryType type);
}