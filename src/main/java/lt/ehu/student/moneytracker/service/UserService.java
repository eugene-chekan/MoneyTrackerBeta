package lt.ehu.student.moneytracker.service;

import lt.ehu.student.moneytracker.model.User;
import java.util.Optional;
import java.util.Set;
import java.util.List;

public interface UserService {
    User register(User user);
    Optional<User> findByLogin(String login);
    Optional<User> findById(Integer id);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    void updateDefaultCurrency(Integer userId, Integer currencyId);
    
    // New role-related methods
    void addRoleToUser(Integer userId, String roleName);
    void removeRoleFromUser(Integer userId, String roleName);
    Set<String> getUserRoles(Integer userId);
    boolean hasRole(Integer userId, String roleName);
    List<User> findAll();
    long count();
} 