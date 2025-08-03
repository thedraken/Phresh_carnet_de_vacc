package com.phresh;

import com.phresh.domain.User;
import com.phresh.exceptions.RuleException;
import com.phresh.repository.UserRepository;
import com.phresh.security.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getSimpleName());

    private final UserRepository userRepository;
    private final PasswordEncryptor passwordEncryptor;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncryptor passwordEncryptor) {
        this.userRepository = userRepository;
        this.passwordEncryptor = passwordEncryptor;
    }

    @Transactional(rollbackFor = RuleException.class)
    public void saveUser(User user) throws RuleException {
        User userCheck = userRepository.findUserByEmail(user.getEmail());
        if (userCheck != null && !Objects.equals(user.getId(), userCheck.getId())) {
            throw new RuleException("User with same email already exists");
        }
        userRepository.save(user);
        logger.config("Saved user: " + user.getId());
    }

    public User doLogin(String username, String password) throws RuleException {
        User user = userRepository.findUserByEmail(username);
        if (user == null || !passwordEncryptor.passwordMatches(password, user.getPassword())) {
            throw new RuleException("Login error");
        }
        return user;
    }
}
