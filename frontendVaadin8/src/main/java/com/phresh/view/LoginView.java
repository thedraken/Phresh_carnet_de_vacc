package com.phresh.view;

import com.google.inject.Inject;
import com.phresh.presenter.LoginPresenter;
import com.vaadin.ui.Button;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginView extends VerticalLayout implements IView<LoginPresenter> {

    public static final String route = "";
    private static final Logger log = LoggerFactory.getLogger(LoginView.class);

    private final LoginPresenter presenter;

    @Inject
    public LoginView(LoginPresenter presenter) {
        this.presenter = presenter;
        this.presenter.setView(this);
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull(); // Use entire window
        addComponent(content);   // Attach to the UI

        LoginForm loginForm = new LoginForm();
        loginForm.setUsernameCaption("Email");
        loginForm.setPasswordCaption("Password");
        loginForm.addLoginListener(loginEvent -> {
            loginForm.setComponentError(null);
            presenter.doLogin(loginEvent.getLoginParameter("username"), loginEvent.getLoginParameter("password"));
        });
        Button createUserButton = new Button("Create User", (Button.ClickListener) evenet -> UI.getCurrent().getNavigator().navigateTo(CreateUserView.route));
        //loginForm
        content.addComponent(loginForm);
        content.addComponent(createUserButton);
    }

    @Override
    public LoginPresenter getPresenter() {
        return presenter;
    }
}
