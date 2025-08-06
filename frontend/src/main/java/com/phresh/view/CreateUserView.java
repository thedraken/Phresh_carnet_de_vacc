package com.phresh.view;

import com.phresh.domain.User;
import com.phresh.presenter.CreateUserPresenter;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Route(value = "createUser", autoLayout = false)
@PageTitle("Create User")
@AnonymousAllowed
public class CreateUserView extends Main implements IPhreshView<CreateUserPresenter> {

    private static final Logger logger = Logger.getLogger(CreateUserView.class.getSimpleName());

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
        DatePicker dateOfBirth = new DatePicker("Date of birth");
        //Would anyone ever be older than 200 years who needs to create a user?
        dateOfBirth.setMin(LocalDate.now().minusYears(200));
        dateOfBirth.setMax(LocalDate.now());
        PasswordField password = new PasswordField("Password");
        password.setMinLength(15);
        //https://stackoverflow.com/questions/1559751/regex-to-make-sure-that-the-string-contains-at-least-one-lower-case-char-upper
        //password.setPattern("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)");
        password.setI18n(new PasswordField.PasswordFieldI18n().setMinLengthErrorMessage("Minimum length is 15 characters").setPatternErrorMessage("Passwords must contain at least one digit, one lower case letter, one upper case letter, and one special character"));
        //password.setHelperText("Passwords must be 15 characters long, containing at least one digit, one lower case letter, one upper case letter, and one special character");
        PasswordField confirmPassword = new PasswordField("Confirm Password");
        confirmPassword.setErrorMessage("Confirm Password is Required");
        Div beanValidationErrors = new Div();
        beanValidationErrors.addClassName(LumoUtility.TextColor.ERROR);

        binder = new Binder<>();
        binder.forField(firstName).asRequired(e -> "First Name is Required").bind(User::getFirstName, User::setFirstName);
        binder.forField(surname).asRequired(e -> "Surname is Required").bind(User::getSurname, User::setSurname);
        binder.forField(email).asRequired(e -> "Email is required").bind(User::getEmail, User::setEmail);
        binder.forField(password).asRequired(e -> "Password is required").bind(User::getPassword, User::setPassword);
        binder.forField(confirmPassword).asRequired(e -> "Password is required").withValidator((Validator<String>) (s, valueContext) -> s.equals(password.getValue()) ? ValidationResult.ok() : ValidationResult.error("Passwords do not match"));
        binder.forField(dateOfBirth).asRequired(e -> "Date of birth is required").bind(User::getDateOfBirth, User::setDateOfBirth);
        binder.setStatusLabel(beanValidationErrors);

        createUserForm.addFormRow(firstName, surname);
        createUserForm.addFormRow(email);
        createUserForm.addFormRow(password, confirmPassword);
        createUserForm.setSizeFull();
        createUserForm.addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.CENTER,
                LumoUtility.AlignItems.CENTER);

        Button createButton = new Button("Create", buttonClickEvent -> validateForm());
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", buttonClickEvent -> {
            binder.getFields().forEach(HasValue::clear);
            UI.getCurrent().navigate(LoginView.class);
        });

        HorizontalLayout buttons = new HorizontalLayout(createButton, cancelButton);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(createUserForm, beanValidationErrors, buttons);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.setSizeFull();

        add(verticalLayout);
    }

    private void validateForm() {
        User user = new User();
        if (binder.writeBeanIfValid(user)) {
            try {
                presenter.createUser(user);
                Map<String, List<String>> mapOfParameters = new HashMap<>();
                mapOfParameters.put("createUser", new ArrayList<>());
                this.getUI().ifPresent(ui -> ui.navigate("login", new QueryParameters(mapOfParameters)));
            } catch (Exception e) {
                Notification notification = Notification.show(e.getMessage());
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }
}
