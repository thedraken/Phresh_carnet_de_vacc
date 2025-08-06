package com.phresh;

import com.phresh.domain.User;
import com.phresh.exceptions.RuleException;
import com.phresh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getSimpleName());

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = RuleException.class)
    public void saveUser(User user) throws RuleException {
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new RuleException("Please enter full name");
        }
        if (user.getSurname() == null || user.getSurname().trim().isEmpty()) {
            throw new RuleException("Please enter surname");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RuleException("Please enter email");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuleException("Please enter password");
        }
        if (user.getRoles().isEmpty()) {
            throw new RuleException("Missing role, please contact support");
        }
        User userCheck = userRepository.findUserByEmail(user.getEmail());
        if (userCheck != null && !Objects.equals(user.getId(), userCheck.getId())) {
            throw new RuleException("User with same email already exists");
        }
        userRepository.save(user);
        logger.config("Saved user: " + user.getId());
    }

    @Transactional(rollbackFor = RuleException.class)
    public void deleteUser(User user) throws RuleException {
        user.setEnabled(false);
        saveUser(user);
    }
}
