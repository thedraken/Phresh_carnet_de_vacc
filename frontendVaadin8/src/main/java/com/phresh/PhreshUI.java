package com.phresh;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.phresh.view.CreateUserView;
import com.phresh.view.LoginView;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import javax.servlet.annotation.WebServlet;
import java.sql.SQLException;

public class PhreshUI extends UI {

    private Navigator navigator;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        navigator = new Navigator(this, this);

        Injector injector = Guice.createInjector(new PhreshModule());
        SQLLiteConfigure sqlLiteConfigure = injector.getInstance(SQLLiteConfigure.class);
        try {
            sqlLiteConfigure.createDatabase();

            navigator.addView(LoginView.route, injector.getInstance(LoginView.class));
            navigator.addView(CreateUserView.route, injector.getInstance(CreateUserView.class));
            navigator.navigateTo("");
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @WebServlet(urlPatterns = "/*", name = "PhreshUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = PhreshUI.class, productionMode = true)
    public static class PhreshUIServlet extends VaadinServlet {

    }
}
