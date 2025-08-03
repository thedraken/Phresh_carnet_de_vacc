package com.phresh.view;

import com.phresh.presenter.MyProfilePresenter;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route("my_profile")
@PermitAll
@PageTitle("My Profile")
public class MyProfileView extends AbstractLoggedInView<MyProfilePresenter> {

    @Autowired
    public MyProfileView(MyProfilePresenter presenter) {
        super(presenter);
    }

    @Override
    public VerticalLayout buildLayout() {
        return new VerticalLayout();
    }
}
