package com.phresh.view;

import com.phresh.domain.User;
import com.phresh.presenter.CreateUserPresenter;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "createUser", autoLayout = false)
@PageTitle("Create User")
@AnonymousAllowed
public class CreateUserView extends Main implements IPhreshView<CreateUserPresenter> {

    private final CreateUserPresenter presenter;

    private final Binder<User> binder;

    @Autowired
    public CreateUserView(CreateUserPresenter presenter) {
        this.presenter = presenter;
        addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.CENTER,
                LumoUtility.AlignItems.CENTER);
        FormLayout createUserForm = new FormLayout();
        createUserForm.setAutoResponsive(true);
        TextField firstName = new TextField("First Name");
        TextField surname = new TextField("Surname");
        TextField email = new TextField("Email");
        PasswordField password = new PasswordField("Password");
        password.setMinLength(15);
        //https://stackoverflow.com/questions/1559751/regex-to-make-sure-that-the-string-contains-at-least-one-lower-case-char-upper
        //password.setPattern("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)");
        password.setI18n(new PasswordField.PasswordFieldI18n().setMinLengthErrorMessage("Minimum length is 15 characters").setPatternErrorMessage("Passwords must contain at least one digit, one lower case letter, one upper case letter, and one special character"));
        //password.setHelperText("Passwords must be 15 characters long, containing at least one digit, one lower case letter, one upper case letter, and one special character");

        PasswordField confirmPassword = new PasswordField("Confirm Password");

        binder = new Binder<>();
        binder.forField(firstName).asRequired().bind(User::getFirstName, User::setFirstName);
        binder.forField(surname).asRequired().bind(User::getSurname, User::setSurname);
        binder.forField(email).asRequired().bind(User::getEmail, User::setEmail);
        binder.forField(password).asRequired().bind(User::getPassword, User::setPassword);
        binder.forField(confirmPassword).asRequired().withValidator((Validator<String>) (s, valueContext) -> s.equals(password.getValue()) ? ValidationResult.ok() : ValidationResult.error("Passwords do not match"));

        createUserForm.addFormRow(firstName, surname);
        createUserForm.addFormRow(email);
        createUserForm.addFormRow(password, confirmPassword);

        Button createButton = new Button("Create", buttonClickEvent -> validateForm());
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", buttonClickEvent -> binder.getFields().forEach(HasValue::clear));

        HorizontalLayout buttons = new HorizontalLayout(createButton, cancelButton);

        add(createUserForm, buttons);
    }

    private void validateForm() {
        User user = new User();
        if (binder.writeBeanIfValid(user)) {
            presenter.createUser(user);
        }
    }
}
