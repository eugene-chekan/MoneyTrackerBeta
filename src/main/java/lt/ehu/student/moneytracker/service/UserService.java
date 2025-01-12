package lt.ehu.student.moneytracker.service;

import lt.ehu.student.moneytracker.model.User;
import java.util.Optional;
import java.util.List;

public interface UserService {
    User register(User user);
    Optional<User> findByLogin(String login);
    Optional<User> findById(Integer id);
    User getById(Integer id);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    void updateDefaultCurrency(Integer userId, Integer currencyId);
    
    // Simplified role methods
    void updateUserRole(Integer userId, String roleName);
    String getUserRole(Integer userId);
    boolean hasRole(Integer userId, String roleName);
    
    List<User> findAll();
    long count();
    void validateAccess(Integer userId, Integer targetUserId);
}