package com.phresh.view;

import com.phresh.domain.User;
import com.phresh.exceptions.RuleException;
import com.phresh.presenter.MyProfilePresenter;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

@Route("my_profile")
@PermitAll
@PageTitle("My Profile")
public class MyProfileView extends AbstractLoggedInView<MyProfilePresenter> implements AfterNavigationObserver {

    private static final Logger logger = Logger.getLogger(MyProfileView.class.getSimpleName());

    @Autowired
    public MyProfileView(MyProfilePresenter presenter) {
        super(presenter);
    }

    @Override
    public VerticalLayout buildLayout() {
        VerticalLayout formLayout = new VerticalLayout();
        TextField firstNameField = new TextField("First Name");
        firstNameField.setWidth("50%");
        firstNameField.setReadOnly(true);
        TextField surnameField = new TextField("Surname");
        surnameField.setWidth("50%");
        surnameField.setReadOnly(true);
        TextField emailField = new TextField("Email");
        emailField.setWidth("50%");
        emailField.setReadOnly(true);
        DatePicker birthDatePicker = new DatePicker("Date of Birth");
        birthDatePicker.setWidth("50%");
        birthDatePicker.setReadOnly(true);
        Button deleteUserButton = new Button("Delete", event ->
                new ConfirmDialog("Please confirm", "Are you sure you want to delete your profile?", "Delete", confirmEvent -> {
                    try {
                        presenter.doDeleteUser();
                        Notification notification = Notification.show("User deleted successfully", 5000, Notification.Position.MIDDLE);
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    } catch (RuleException e) {
                        showNotificationError(e);
                    }
                }, "Cancel", cancelEvent -> cancelEvent.getSource().close()).open());

        HorizontalLayout nameLayout = new HorizontalLayout(firstNameField, surnameField);
        nameLayout.setWidth("50%");
        HorizontalLayout otherLayout = new HorizontalLayout(emailField, birthDatePicker);
        otherLayout.setWidth("50%");
        formLayout.add(nameLayout, otherLayout, deleteUserButton);

        User user = presenter.getUserFromAuthenticationContext();
        Binder<User> binder = new Binder<>(User.class);
        binder.setBean(user);
        binder.forField(firstNameField).bind(User::getFirstName, User::setFirstName);
        binder.forField(surnameField).bind(User::getSurname, User::setSurname);
        binder.forField(emailField).bind(User::getEmail, User::setEmail);
        binder.forField(birthDatePicker).bind(User::getDateOfBirth, User::setDateOfBirth);

        formLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        formLayout.setSizeFull();

        return formLayout;
    }


    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        logger.info("Entering " + MyProfileView.class.getSimpleName());
    }
}
