package lt.ehu.student.moneytracker.service.impl;

import lt.ehu.student.moneytracker.model.Role;
import lt.ehu.student.moneytracker.model.User;
import lt.ehu.student.moneytracker.repository.UserRepository;
import lt.ehu.student.moneytracker.service.RoleService;
import lt.ehu.student.moneytracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, 
                         RoleService roleService,
                         PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setRegistrationDate(LocalDateTime.now());
        
        // Add default USER to new users
        roleService.findByName("USER").ifPresent(role -> 
            user.getRoles().add(role)
        );
        
        return userRepository.save(user);
    }

    @Override
    public void addRoleToUser(Integer userId, String roleName) {
        userRepository.findById(userId).ifPresent(user -> 
            roleService.findByName(roleName).ifPresent(role ->
                user.getRoles().add(role)
            )
        );
    }

    @Override
    public void removeRoleFromUser(Integer userId, String roleName) {
        userRepository.findById(userId).ifPresent(user ->
            roleService.findByName(roleName).ifPresent(role ->
                user.getRoles().remove(role)
            )
        );
    }

    @Override
    public Set<String> getUserRoles(Integer userId) {
        return userRepository.findById(userId)
            .map(user -> user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()))
            .orElse(Set.of());
    }

    @Override
    public boolean hasRole(Integer userId, String roleName) {
        return userRepository.findById(userId)
            .map(user -> user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(roleName)))
            .orElse(false);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
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
    public void updateDefaultCurrency(Integer userId, Integer currencyId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setDefaultCurrency(currencyId);
            userRepository.save(user);
        });
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public long count() {
        return userRepository.count();
    }
} 