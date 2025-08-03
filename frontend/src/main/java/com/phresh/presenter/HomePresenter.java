package com.phresh.presenter;

import com.phresh.view.HomeView;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HomePresenter extends AbstractLoggedinPresenter<HomeView> {

    @Autowired
    public HomePresenter(AuthenticationContext authenticationContext) {
        super(authenticationContext);
    }
}
