package com.phresh.view;

import com.phresh.domain.User;
import com.phresh.presenter.AbstractLoggedinPresenter;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public abstract class AbstractLoggedInView<P extends AbstractLoggedinPresenter<?>> extends AppLayout implements IPhreshView<P>, BeforeEnterObserver {

    protected final P presenter;

    public AbstractLoggedInView(P presenter) {
        this.presenter = presenter;
        User user = presenter.getUserFromAuthenticationContext();
        HorizontalLayout header;
        H1 logo = new H1("Phresh Vaccination schedule");
        logo.addClassName("logo");
        if (user != null) {
            Span loggedUser = new Span("Welcome " + user.getFullName());
            MenuBar menuBar = new MenuBar();
            MenuItem vaccCardMenuItem = menuBar.addItem(new Icon(VaadinIcon.NEWSPAPER), "Vaccination Card", (ComponentEventListener<ClickEvent<MenuItem>>) menuItemClickEvent -> UI.getCurrent().navigate(VaccinationCardView.class));
            vaccCardMenuItem.setAriaLabel("Vaccination Card");
            MenuItem profileMenuItem = menuBar.addItem(new Icon(VaadinIcon.USER), "My Profile", (ComponentEventListener<ClickEvent<MenuItem>>) menuItemClickEvent -> UI.getCurrent().navigate(MyProfileView.class));
            profileMenuItem.setAriaLabel("My Profile");
            menuBar.addItem("Logout", (ComponentEventListener<ClickEvent<MenuItem>>) menuItemClickEvent -> presenter.doLogout());
            header = new HorizontalLayout(logo, loggedUser, menuBar);
            header.setSizeFull();
            header.setFlexGrow(1, logo);
            addToNavbar(header);
            buildLayout();
        } else {
            UI.getCurrent().navigate(LoginView.class);
        }
    }

    public abstract VerticalLayout buildLayout();

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey(LoginView.LOGGED_IN_MESSAGE)) {
            Notification.show("Logged in Successfully");
        }
    }
}
