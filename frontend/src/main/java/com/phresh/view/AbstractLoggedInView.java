package com.phresh.view;

import com.phresh.domain.User;
import com.phresh.presenter.AbstractLoggedinPresenter;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractLoggedInView<P extends AbstractLoggedinPresenter<?>> extends AppLayout implements IPhreshView<P> {

    private final P presenter;

    @Autowired
    public AbstractLoggedInView(P presenter) {
        this.presenter = presenter;
        User user = presenter.getUserFromAuthenticationContext();

        HorizontalLayout header;
        H1 logo = new H1("Vaadin CRM");
        logo.addClassName("logo");
        try {
            if (user != null) {
                Button logout = new Button("Logout", click ->
                        this.presenter.doLogout());
                Span loggedUser = new Span("Welcome " + user.getFullName());
                header = new HorizontalLayout(logo, loggedUser, logout);
            } else {
                header = new HorizontalLayout(logo);
            }
        } catch (Exception ex) {
            header = new HorizontalLayout(logo);
        }
        addToNavbar(header);
        buildLayout();
    }

    public abstract VerticalLayout buildLayout();
}
