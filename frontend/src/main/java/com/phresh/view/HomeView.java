package com.phresh.view;

import com.phresh.presenter.HomePresenter;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@PageTitle("Phresh Vaccination")
@PermitAll
public class HomeView extends AbstractLoggedInView<HomePresenter> {

    @Autowired
    public HomeView(HomePresenter presenter) {
        super(presenter);
    }

    @Override
    public VerticalLayout buildLayout() {
        Notification.show("Login successful");
        return new VerticalLayout();
    }
}
