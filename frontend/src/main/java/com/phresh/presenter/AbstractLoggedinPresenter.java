package com.phresh.presenter;

import com.phresh.domain.User;
import com.phresh.view.IPhreshView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractLoggedinPresenter<V extends IPhreshView<?>> implements IPhreshPresenter<V> {

    private static final String LOGOUT_SUCCESS_URL = "/login";

    private final AuthenticationContext authenticationContext;

    @Autowired
    public AbstractLoggedinPresenter(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    public User getUserFromAuthenticationContext() {
        return authenticationContext.getAuthenticatedUser(User.class).orElse(null);
    }

    public void doLogout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
        authenticationContext.logout();
    }
}
