package com.phresh.presenter;

import com.google.inject.Inject;
import com.phresh.UserService;
import com.phresh.domain.Role;
import com.phresh.domain.User;
import com.phresh.exceptions.RuleException;
import com.phresh.repository.RoleRepository;
import com.phresh.security.PasswordEncryptor;
import com.phresh.view.CreateUserView;

import java.time.LocalDate;
import java.util.Collections;

public class CreateUserPresenter implements IPresenter<CreateUserView> {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncryptor passwordEncoder;

    private CreateUserView view;

    @Inject
    public CreateUserPresenter(UserService userService, RoleRepository roleRepository, PasswordEncryptor passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CreateUserView getView() {
        return view;
    }

    @Override
    public void setView(CreateUserView view) {
        this.view = view;
    }

    public void doCreateUser(String firstName, String surname, String email, String password, String confirmPassword, LocalDate dateOfBirth) throws RuleException {
        try {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new RuleException("First name is required");
        }
        if (surname == null || surname.trim().isEmpty()) {
            throw new RuleException("Surname is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new RuleException("Email is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new RuleException("Password is required");
        }
        if (confirmPassword == null || confirmPassword.trim().isEmpty() || !password.trim().equals(confirmPassword.trim())) {
            throw new RuleException("Passwords do not match");
        }

            //password = passwordEncoder.encode(password);

        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now()) || dateOfBirth.isBefore(LocalDate.now().minusYears(200))) {
            throw new RuleException("Date of birth is required/invalid");
        }
        Role role = roleRepository.findRoleByName("member");
        User user = new User(firstName, surname, email, password, Collections.singleton(role), dateOfBirth);
        userService.saveUser(user);
        } catch (Exception /*InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException |
                 InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException*/ e) {
            throw new RuleException("Error saving user", e);
        }
    }
}
