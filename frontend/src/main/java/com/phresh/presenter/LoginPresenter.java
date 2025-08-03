package com.phresh.presenter;

import com.phresh.UserService;
import com.phresh.exceptions.RuleException;
import com.phresh.security.SecurityService;
import com.phresh.view.LoginView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;

@Component
public class LoginPresenter implements IPhreshPresenter<LoginView> {

    private final UserService userService;
    private final SecurityService securityService;

    @Autowired
    public LoginPresenter(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    public boolean login(String username, String password) throws RuleException, LoginException {
        return securityService.login(userService.doLogin(username, password));
    }

}
