package com.phresh.security;

import com.phresh.UserService;
import com.phresh.domain.User;
import com.phresh.exceptions.RuleException;
import com.phresh.repository.UserRepository;
import org.springframework.beans.BeanUtils;
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
    private final PasswordEncryptor passwordEncryptor;

    @Autowired
    public PhreshUserDetailsManager(UserService userService, UserRepository userRepository, PasswordEncryptor passwordEncryptor) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncryptor = passwordEncryptor;
    }

    @Override
    public void createUser(UserDetails userDetails) {
        //TODO
        //userService.
    }

    @Override
    public void updateUser(UserDetails userDetails) {
        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
        User user = userRepository.findUserByEmailAndEnabledTrue(currentUser.getPrincipal().toString());
        if (currentUser instanceof User updatedUser) {
            BeanUtils.copyProperties(user, updatedUser);
            try {
                userService.saveUser(user);
            } catch (RuleException e) {
                //TODO
            }
        } else {
            //TODO Does this happen?
        }
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepository.findUserByEmail(username);
        user.setEnabled(false);
        try {
            userService.saveUser(user);
        } catch (RuleException e) {
            //TODO
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
        User user = userRepository.findUserByEmailAndEnabledTrue(currentUser.getPrincipal().toString());
        if (user != null && user.getPassword().equals(passwordEncryptor.encode(oldPassword))) {
            user.setPassword(passwordEncryptor.encode(newPassword));
            try {
                userService.saveUser(user);
            } catch (RuleException e) {
                //TODO
            }
        }
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findUserByEmailAndEnabledTrue(username) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmailAndEnabledTrue(username);
    }

    public PasswordEncryptor getPasswordEncryptor() {
        return passwordEncryptor;
    }
}
