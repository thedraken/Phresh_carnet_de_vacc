package com.phresh.view;

import com.phresh.exceptions.RuleException;
import com.phresh.presenter.LoginPresenter;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Route(value = "login", autoLayout = false)
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends Main implements BeforeEnterObserver, IPhreshView<LoginPresenter> {

    public static final String LOGIN_PATH = "login-action";
    public static final String LOGGED_IN_MESSAGE = "loggedIn";
    private static final Logger logger = Logger.getLogger(LoginView.class.getSimpleName());
    private final LoginForm loginForm;
    private final AuthenticationContext authenticationContext;
    //private final LoginPresenter loginPresenter;

    @Autowired
    public LoginView(LoginPresenter loginPresenter, AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
        //this.loginPresenter = loginPresenter;
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.CENTER,
                LumoUtility.AlignItems.CENTER);
        verticalLayout.setSizeFull();
        LoginI18n loginI18n = LoginI18n.createDefault();
        LoginI18n.Form loginI18nForm = loginI18n.getForm();
        loginI18nForm.setUsername("Email");
        loginForm = new LoginForm(loginI18n);
        //loginForm.setAction("login");
        loginForm.addLoginListener((ComponentEventListener<AbstractLogin.LoginEvent>) loginEvent -> {
            loginForm.setError(false);
            try {
                loginPresenter.login(loginEvent.getUsername(), loginEvent.getPassword());
                QueryParameters loggedInQueryParameter = new QueryParameters(Map.of(LOGGED_IN_MESSAGE, new ArrayList<>()));
                UI.getCurrent().navigate(VaccinationCardView.class, loggedInQueryParameter);
                loginForm.setError(false);
            } catch (RuleException | LoginException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                loginForm.setError(true);
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
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("createUser")) {
            Notification.show("User created successfully");
        }
        if (authenticationContext.isAuthenticated()) {
            // Redirect to the main view if the user is already logged in. This makes impersonation easier to work with.
            beforeEnterEvent.forwardTo("");
        }
    }
}
