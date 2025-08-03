package com.phresh.presenter;

import com.phresh.view.VaccinationCardView;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VaccinationCardPresenter extends AbstractLoggedinPresenter<VaccinationCardView> {

    @Autowired
    public VaccinationCardPresenter(AuthenticationContext authenticationContext) {
        super(authenticationContext);
    }
}
