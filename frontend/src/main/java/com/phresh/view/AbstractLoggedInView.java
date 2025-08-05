package com.phresh.view;

import com.phresh.domain.User;
import com.phresh.presenter.AbstractLoggedinPresenter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public abstract class AbstractLoggedInView<P extends AbstractLoggedinPresenter<?>> extends AppLayout implements IPhreshView<P>, BeforeEnterObserver {

    protected final P presenter;

    public AbstractLoggedInView(P presenter) {
        this.presenter = presenter;
        User user = presenter.getUserFromAuthenticationContext();
        if (user != null) {
            H1 logo = new H1("Phresh Vaccination schedule");
            logo.addClassName("logo");
            Span loggedUser = new Span("Welcome " + user.getFullName());
            SideNav sideNav = new SideNav();
            SideNavItem vaccinationCardSideItem = new SideNavItem("Vaccination Card", VaccinationCardView.class, VaadinIcon.CALENDAR.create());
            SideNavItem myProfileSideItem = new SideNavItem("My Profile", MyProfileView.class, VaadinIcon.USER.create());
            sideNav.addItem(vaccinationCardSideItem, myProfileSideItem);
            Button logout = new Button("Logout", buttonClickEvent -> presenter.doLogout());
            HorizontalLayout header = new HorizontalLayout(logo, loggedUser, logout);
            header.setSizeFull();
            header.setFlexGrow(1, logo);
            addToNavbar(header);
            addToDrawer(sideNav);
            setContent(buildLayout());
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
