package com.phresh.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;

public class MainLayout extends AppLayout {
    private final transient AuthenticationContext authenticationContext;

    public MainLayout(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;

        H1 logo = new H1("Vaadin CRM");
        logo.addClassName("logo");
        HorizontalLayout
                header =
                authenticationContext.getAuthenticatedUser(UserDetails.class)
                        .map(user -> {
                            Button logout = new Button("Logout", click ->
                                    this.authenticationContext.logout());
                            Span loggedUser = new Span("Welcome " + user.getUsername());
                            return new HorizontalLayout(logo, loggedUser, logout);
                        }).orElseGet(() -> new HorizontalLayout(logo));
        addToNavbar(header);
    }
}
