package com.phresh.presenter;

import com.phresh.UserService;
import com.phresh.exceptions.RuleException;
import com.phresh.security.PasswordEncryptor;
import com.phresh.view.LoginView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginPresenter implements IPhreshPresenter<LoginView> {

    private final UserService userService;

    @Autowired
    public LoginPresenter(UserService userService) {
        this.userService = userService;
    }

    public void login(String username, String password) throws RuleException {
        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
        passwordEncryptor.encryptPassword(password);
        userService.doLogin(username, password);
    }

}
