package lt.ehu.student.moneytracker.service.impl;

import lt.ehu.student.moneytracker.model.User;
import lt.ehu.student.moneytracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);
        
        User user = userRepository.findByLogin(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        log.debug("Found user: {}", user.getLogin());
        
        // Create authority from single role
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());
        
        log.debug("User authority: {}", authority);

        return new org.springframework.security.core.userdetails.User(
            user.getLogin(),
            user.getPasswordHash(),
            Collections.singletonList(authority)
        );
    }
} 