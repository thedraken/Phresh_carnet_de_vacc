package com.phresh.presenter;

import com.phresh.exceptions.RuleException;
import com.phresh.security.SecurityService;
import com.phresh.view.LoginView;
import com.vaadin.flow.component.ComponentEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

@Component
public class LoginPresenter implements IPhreshPresenter<LoginView> {

    private final SecurityService securityService;
    private final List<ComponentEventListener<?>> componentEventListeners = new ArrayList<>();

    @Autowired
    public LoginPresenter(SecurityService securityService) {
        this.securityService = securityService;


    }

    public void login(String username, String password) throws RuleException, LoginException {
        securityService.login(username, password);
    }

}
