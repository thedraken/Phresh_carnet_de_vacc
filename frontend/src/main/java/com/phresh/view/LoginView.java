package com.phresh.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route(value = "login", autoLayout = false)
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends Main implements BeforeEnterObserver {

    private final LoginForm loginForm;

    public LoginView() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.CENTER,
                LumoUtility.AlignItems.CENTER);
        verticalLayout.setSizeFull();
        loginForm = new LoginForm();
        loginForm.setAction("login");

        Button createUserButton = new Button("Create User", event -> this.getUI().ifPresent(ui -> ui.navigate("createUser")));
        HorizontalLayout buttonLayout = new HorizontalLayout(createUserButton);
        verticalLayout.add(loginForm, buttonLayout);
        add(verticalLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
