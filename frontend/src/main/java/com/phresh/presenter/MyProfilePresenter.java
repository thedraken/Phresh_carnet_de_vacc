package com.phresh.presenter;

import com.phresh.UserService;
import com.phresh.exceptions.RuleException;
import com.phresh.repository.VaccinationScheduleRepository;
import com.phresh.view.MyProfileView;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyProfilePresenter extends AbstractLoggedinPresenter<MyProfileView> {

    private final UserService userService;

    @Autowired
    public MyProfilePresenter(AuthenticationContext authenticationContext, UserService userService, VaccinationScheduleRepository vaccinationScheduleRepository) {
        super(authenticationContext, vaccinationScheduleRepository);
        this.userService = userService;
    }

    public void doDeleteUser() throws RuleException {
        userService.deleteUser(getUserFromAuthenticationContext());
        doLogout();
    }
}
