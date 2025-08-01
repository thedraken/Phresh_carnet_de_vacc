package com.phresh.view;

import com.phresh.presenter.LoginPresenter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "login", autoLayout = false)
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends Main implements BeforeEnterObserver, IPhreshView<LoginPresenter> {

    private final LoginForm loginForm;
    private final LoginPresenter loginPresenter;

    @Autowired
    public LoginView(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.CENTER,
                LumoUtility.AlignItems.CENTER);
        verticalLayout.setSizeFull();
        LoginI18n loginI18n = LoginI18n.createDefault();
        LoginI18n.Form loginI18nForm = loginI18n.getForm();
        loginI18nForm.setUsername("Email");
        loginForm = new LoginForm(loginI18n);
        loginForm.setAction("login");
        loginForm.addLoginListener(event -> {
            try {
                loginPresenter.login(event.getUsername(), event.getPassword());
                this.getUI().ifPresent(ui -> ui.navigate(""));
            } catch (Exception e) {
                Notification notification = new Notification(e.getMessage());
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
            }
        });


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
