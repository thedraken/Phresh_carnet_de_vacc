package com.phresh;

import com.google.inject.Inject;
import com.phresh.domain.User;
import com.phresh.exceptions.RuleException;
import com.phresh.repository.UserRepository;

import java.util.Objects;
import java.util.logging.Logger;


public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getSimpleName());

    private final UserRepository userRepository;

    @Inject
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void saveUser(User user) throws RuleException {
        if (user == null) {
            throw new RuleException("Missing user details");
        }
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new RuleException("Please enter first name");
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
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new RuleException("Missing role, please contact support");
        }
        User userCheck = userRepository.findUserByEmail(user.getEmail());
        if (userCheck != null && !Objects.equals(user.getId(), userCheck.getId())) {
            throw new RuleException("User with same email already exists");
        }
        userRepository.save(user);
        logger.config("Saved user: " + user.getId());
    }

    public void deleteUser(User user) throws RuleException {
        if (user == null) {
            throw new RuleException("Missing user details");
        }
        user.setEnabled(false);
        saveUser(user);
    }
}
