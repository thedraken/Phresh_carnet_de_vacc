package com.phresh.presenter;

import com.phresh.view.MyProfileView;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyProfilePresenter extends AbstractLoggedinPresenter<MyProfileView> {

    @Autowired
    public MyProfilePresenter(AuthenticationContext authenticationContext) {
        super(authenticationContext);
    }
}
