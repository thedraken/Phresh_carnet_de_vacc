package com.phresh.view;

import com.phresh.presenter.MyProfilePresenter;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
        return new VerticalLayout();
    }


    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        logger.info("Entering " + MyProfileView.class.getSimpleName());
    }
}
