package com.phresh.security;

import com.phresh.UserService;
import com.phresh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class PhreshUserDetailsManager implements UserDetailsManager {

    private final UserService userService;
    private final UserRepository userRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Autowired
    public PhreshUserDetailsManager(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(UserDetails userDetails) {
        //userService.
    }

    @Override
    public void updateUser(UserDetails user) {
        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
    }

    @Override
    public void deleteUser(String username) {
        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findUserByEmail(username) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username);
    }
}
