package com.phresh.security;

import com.phresh.domain.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.authentication.jaas.SecurityContextLoginModule;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;

@Component
public class SecurityService {
    private static final String LOGOUT_SUCCESS_URL = "/";
    //private static final Logger log = LoggerFactory.getLogger(SecurityService.class);

    public UserDetails getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) context.getAuthentication().getPrincipal();
        }
        // Anonymous or no authentication.
        return null;
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
    }

    public boolean login(User user) throws LoginException {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(user);
        SecurityContextLoginModule loginModule = new SecurityContextLoginModule();
        return loginModule.login();
    }
}
