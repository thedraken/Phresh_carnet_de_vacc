package com.phresh.presenter;

import com.phresh.UserService;
import com.phresh.domain.User;
import com.phresh.exceptions.RuleException;
import com.phresh.security.PasswordEncryptor;
import com.phresh.view.CreateUserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class CreateUserPresenter implements IPhreshPresenter<CreateUserView> {

    private final PasswordEncryptor passwordEncryptor;
    private final UserService userService;

    @Autowired
    public CreateUserPresenter(PasswordEncryptor passwordEncryptor, UserService userService) {
        this.passwordEncryptor = passwordEncryptor;
        this.userService = userService;
    }

    public void createUser(User user) throws RuleException {
        Pattern pattern = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)");
        if (!pattern.matcher(user.getPassword()).find()) {
            throw new RuleException("Passwords must contain at least one digit, one lower case letter, one upper case letter, and one special character");
        }
        user.setPassword(passwordEncryptor.encode(user.getPassword()));
        userService.saveUser(user);
    }
    
}
