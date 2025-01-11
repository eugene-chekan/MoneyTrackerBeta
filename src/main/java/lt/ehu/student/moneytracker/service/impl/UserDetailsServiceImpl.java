package lt.ehu.student.moneytracker.service.impl;

import lt.ehu.student.moneytracker.model.User;
import lt.ehu.student.moneytracker.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to load user by username: {}", username);
        
        try {
            User user = userService.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            log.debug("Found user: {}", user.getLogin());
            log.debug("User roles from DB: {}", user.getRoles());
            
            var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
            
            log.debug("Mapped authorities: {}", authorities);

            return org.springframework.security.core.userdetails.User
                .withUsername(user.getLogin())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .build();
        } catch (Exception e) {
            log.error("Error loading user {}: {}", username, e.getMessage(), e);
            throw e;
        }
    }
} 