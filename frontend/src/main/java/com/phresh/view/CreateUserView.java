package com.phresh.view;

import com.phresh.presenter.CreateUserPresenter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route(value = "createUser", autoLayout = false)
@PageTitle("Create User")
@AnonymousAllowed
public class CreateUserView extends Main implements IPhreshView<CreateUserPresenter> {

    private final FormLayout createUserForm;

    public CreateUserView() {
        addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.CENTER,
                LumoUtility.AlignItems.CENTER);
        createUserForm = new FormLayout();
        createUserForm.setAutoResponsive(true);

        TextField firstName = new TextField("First Name");
        TextField surname = new TextField("Surname");
        TextField email = new TextField("Email");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm Password");


        createUserForm.addFormRow(firstName, surname);
        createUserForm.addFormRow(email);
        createUserForm.addFormRow(password, confirmPassword);

        Button createButton = new Button("Create");
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel");

        HorizontalLayout buttons = new HorizontalLayout(createButton, cancelButton);

        add(createUserForm, buttons);
    }
}
