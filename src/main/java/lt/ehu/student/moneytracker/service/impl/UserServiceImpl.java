package lt.ehu.student.moneytracker.service.impl;

import lt.ehu.student.moneytracker.model.Currency;
import lt.ehu.student.moneytracker.model.Role;
import lt.ehu.student.moneytracker.model.User;
import lt.ehu.student.moneytracker.repository.UserRepository;
import lt.ehu.student.moneytracker.service.CurrencyService;
import lt.ehu.student.moneytracker.service.RoleService;
import lt.ehu.student.moneytracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lt.ehu.student.moneytracker.exception.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final CurrencyService currencyService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, 
                         RoleService roleService,
                         PasswordEncoder passwordEncoder,
                         CurrencyService currencyService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.currencyService = currencyService;
    }

    @Override
    public User register(User user) {
        validateNewUser(user);
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setRegistrationDate(LocalDateTime.now());
        
        // Set default USER role
        roleService.findByName("USER").ifPresent(user::setRole);
        
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User getById(Integer id) {
        return findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void updateDefaultCurrency(Integer userId, Integer currencyId) {
        User user = getById(userId);
        Currency currency = currencyService.findById(currencyId)
            .orElseThrow(() -> new ResourceNotFoundException("Currency not found"));
        user.setDefaultCurrency(currency);
        userRepository.save(user);
    }

    @Override
    public boolean hasRole(Integer userId, String roleName) {
        return getById(userId).getRole().getName().equals(roleName);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public void validateAccess(Integer userId, Integer targetUserId) {
        if (!userId.equals(targetUserId) && !hasRole(userId, "ADMIN")) {
            throw new AccessDeniedException("User does not have access to this resource");
        }
    }

    @Override
    public void updateUserRole(Integer userId, String roleName) {
        User user = getById(userId);
        Role role = roleService.findByName(roleName)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public String getUserRole(Integer userId) {
        return getById(userId).getRole().getName();
    }

    private void validateNewUser(User user) {
        if (existsByLogin(user.getLogin())) {
            throw new IllegalArgumentException("Login already exists");
        }
        if (existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (user.getLogin() == null || user.getLogin().trim().isEmpty()) {
            throw new IllegalArgumentException("Login cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }
} 