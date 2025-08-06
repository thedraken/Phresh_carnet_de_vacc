package com.phresh.view;

import com.phresh.domain.User;
import com.phresh.domain.VaccinationSchedule;
import com.phresh.exceptions.RuleException;
import com.phresh.presenter.AbstractLoggedinPresenter;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractLoggedInView<P extends AbstractLoggedinPresenter<?>> extends AppLayout implements IPhreshView<P>, BeforeEnterObserver {

    protected final P presenter;

    public AbstractLoggedInView(P presenter) {
        this.presenter = presenter;
        User user = presenter.getUserFromAuthenticationContext();
        if (user != null) {
            H1 logo = new H1("Phresh Vaccination schedule");
            logo.addClassName("logo");
            Span loggedUser = new Span("Welcome " + user.getFullName());
            SideNav sideNav = new SideNav();
            SideNavItem vaccinationCardSideItem = new SideNavItem("Vaccination Card", VaccinationCardView.class, VaadinIcon.CALENDAR.create());
            SideNavItem myProfileSideItem = new SideNavItem("My Profile", MyProfileView.class, VaadinIcon.USER.create());
            SideNavItem upcomingSchedule = new SideNavItem("Upcoming Schedule", ScheduledVaccinationView.class, VaadinIcon.SEARCH.create());
            sideNav.addItem(vaccinationCardSideItem, upcomingSchedule, myProfileSideItem);
            Button logout = new Button("Logout", buttonClickEvent -> presenter.doLogout());
            HorizontalLayout header = new HorizontalLayout(logo, loggedUser, logout);
            header.setSizeFull();
            header.setFlexGrow(1, logo);
            addToNavbar(header);
            addToDrawer(sideNav);
            setContent(buildLayout());
        }
    }

    public abstract VerticalLayout buildLayout();

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey(LoginView.LOGGED_IN_MESSAGE)) {
            Notification.show("Logged in Successfully");
            presenter.getUserFromAuthenticationContext().setSeenFirstNotification(false);
        } else if (presenter.getUserFromAuthenticationContext() == null) {
            UI.getCurrent().navigate(LoginView.class);
        } else {

            AbstractLoggedinPresenter.PendingItems pendingItems = presenter.getPendingVaccinationSchedules();
            if (!presenter.getUserFromAuthenticationContext().isSeenFirstNotification() && !pendingItems.getOutOfDateVaccinationSchedules().isEmpty()) {
                presenter.getUserFromAuthenticationContext().setSeenFirstNotification(true);
                String outOfDateItems = String.join(", ", pendingItems.getOutOfDateVaccinationSchedules().stream().map(vaccinationSchedule ->
                        vaccinationSchedule.getVaccinationType() + " (" + vaccinationSchedule.getScheduledDate() + ")").collect(Collectors.toSet()));
                Notification notification = new Notification();
                Div text = new Div(new Text("You are overdue for some vaccinations: " + outOfDateItems));

                Button closeButton = new Button(VaadinIcon.CLOSE.create());
                closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                closeButton.setAriaLabel("Close");
                closeButton.addClickListener(event -> notification.close());

                HorizontalLayout layout = new HorizontalLayout(text, closeButton);
                layout.setAlignItems(FlexComponent.Alignment.CENTER);
                notification.add(layout);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.open();
            } else if (!pendingItems.getUpcomingVaccinationSchedules().isEmpty()) {
                int maxSize = pendingItems.getUpcomingVaccinationSchedules().size();
                if (maxSize > 5) {
                    maxSize = 5;
                }
                List<VaccinationSchedule> vaccinationScheduleList = pendingItems.getUpcomingVaccinationSchedules().stream().sorted(Comparator.comparing(VaccinationSchedule::getScheduledDate)).collect(Collectors.toList()).subList(0, maxSize - 1);
                Notification.show("Your next vaccinations due are: " +
                        String.join(", ", vaccinationScheduleList.stream().map(vaccinationSchedule ->
                                        vaccinationSchedule.getVaccinationType() + " (" + vaccinationSchedule.getScheduledDate() + ")")
                                .collect(Collectors.toSet())), 3000, Notification.Position.MIDDLE);
            }
        }
    }

    protected void showNotificationError(RuleException e) {
        Notification notification = Notification.show(e.getMessage());
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
