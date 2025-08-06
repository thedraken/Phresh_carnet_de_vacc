package com.phresh.view;

import com.google.inject.Inject;
import com.phresh.exceptions.RuleException;
import com.phresh.presenter.CreateUserPresenter;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.UserError;
import com.vaadin.ui.*;

import java.time.LocalDate;

public class CreateUserView extends VerticalLayout implements IView<CreateUserPresenter> {

    public static final String route = "CreateUser";

    private final CreateUserPresenter createUserPresenter;
    private final FormLayout formLayout = new FormLayout();

    @Inject
    public CreateUserView(CreateUserPresenter createUserPresenter) {
        this.createUserPresenter = createUserPresenter;
        this.createUserPresenter.setView(this);

        setSizeFull();
        formLayout.removeAllComponents();
        TextField firstName = new TextField("First Name");
        firstName.setRequiredIndicatorVisible(true);
        TextField surName = new TextField("Surname");
        surName.setRequiredIndicatorVisible(true);
        TextField email = new TextField("Email");
        email.setRequiredIndicatorVisible(true);
        PasswordField password = new PasswordField("Password");
        password.setRequiredIndicatorVisible(true);
        PasswordField confirmPassword = new PasswordField("Confirm Password");
        confirmPassword.setRequiredIndicatorVisible(true);
        DateField birthday = new DateField("Birthday");
        birthday.setRangeStart(LocalDate.now().minusYears(200));
        birthday.setRangeEnd(LocalDate.now());
        birthday.setRequiredIndicatorVisible(true);
        Button createUserButton = new Button("Create User", (Button.ClickListener) event -> {
            formLayout.setComponentError(null);
            try {
                getPresenter().doCreateUser(firstName.getValue(), surName.getValue(), email.getValue(), password.getValue(), confirmPassword.getValue(), birthday.getValue());
                Notification.show("Successfully created user", Notification.Type.TRAY_NOTIFICATION);
                UI.getCurrent().getNavigator().navigateTo(LoginView.route);
            } catch (RuleException e) {
                formLayout.setComponentError(new UserError(e.getMessage()));
            }
        });
        Button cancelButton = new Button("Cancel", (Button.ClickListener) event -> UI.getCurrent().getNavigator().navigateTo(LoginView.route));

        HorizontalLayout nameLayout = new HorizontalLayout(firstName, surName);
        HorizontalLayout emailLayout = new HorizontalLayout(email, birthday);
        HorizontalLayout passwordLayout = new HorizontalLayout(password, confirmPassword);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponent(createUserButton);
        buttonLayout.addComponent(cancelButton);

        formLayout.addComponents(nameLayout, emailLayout, passwordLayout, buttonLayout);
        addComponent(formLayout);

    }

    @Override
    public CreateUserPresenter getPresenter() {
        return createUserPresenter;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        IView.super.enter(event);
        formLayout.setComponentError(null);
    }
}
