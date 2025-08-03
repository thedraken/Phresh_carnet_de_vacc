package com.phresh.view;

import com.phresh.domain.RoleAccess;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;

/**
 * This view shows up when a user navigates to the root ('/') of the application.
 */
@Route("home")
@RolesAllowed(value = {RoleAccess.ROLE_USER, RoleAccess.ROLE_ADMIN})
//@Layout("MainLayout")
public final class MainView extends Main {

    MainView() {
        addClassName(LumoUtility.Padding.MEDIUM);
    }

    /**
     * Navigates to the main view.
     */
    public static void showMainView() {
        UI.getCurrent().navigate(MainView.class);
    }
}