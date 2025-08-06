package com.phresh.presenter;

import com.phresh.domain.User;
import com.phresh.domain.VaccinationSchedule;
import com.phresh.repository.VaccinationScheduleRepository;
import com.phresh.view.IPhreshView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public abstract class AbstractLoggedinPresenter<V extends IPhreshView<?>> implements IPhreshPresenter<V> {

    private static final String LOGOUT_SUCCESS_URL = "/login";

    private final AuthenticationContext authenticationContext;
    protected final VaccinationScheduleRepository vaccinationScheduleRepository;

    @Autowired
    public AbstractLoggedinPresenter(AuthenticationContext authenticationContext, VaccinationScheduleRepository vaccinationScheduleRepository) {
        this.authenticationContext = authenticationContext;
        this.vaccinationScheduleRepository = vaccinationScheduleRepository;
    }

    public User getUserFromAuthenticationContext() {
        return authenticationContext.getAuthenticatedUser(User.class).orElse(null);
    }

    public void doLogout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
        authenticationContext.logout();
    }

    public PendingItems getPendingVaccinationSchedules() {
        List<VaccinationSchedule> vaccinationSchedules = vaccinationScheduleRepository.findAllByUserAndVaccinationIsNull(getUserFromAuthenticationContext());
        List<VaccinationSchedule> outOfDateVaccinationSchedules = new ArrayList<>();
        List<VaccinationSchedule> upcomingVaccinationSchedules = new ArrayList<>();
        vaccinationSchedules.forEach(vaccinationSchedule -> {
            if (vaccinationSchedule.getScheduledDate().isBefore(LocalDate.now())) {
                outOfDateVaccinationSchedules.add(vaccinationSchedule);
            } else {
                upcomingVaccinationSchedules.add(vaccinationSchedule);
            }
        });
        return new PendingItems(outOfDateVaccinationSchedules, upcomingVaccinationSchedules);
    }

    public static class PendingItems {
        private final List<VaccinationSchedule> outOfDateVaccinationSchedules;
        private final List<VaccinationSchedule> upcomingVaccinationSchedules;

        public PendingItems(List<VaccinationSchedule> outOfDateVaccinationSchedules, List<VaccinationSchedule> upcomingVaccinationSchedules) {
            this.outOfDateVaccinationSchedules = outOfDateVaccinationSchedules;
            this.upcomingVaccinationSchedules = upcomingVaccinationSchedules;
        }

        public List<VaccinationSchedule> getOutOfDateVaccinationSchedules() {
            return outOfDateVaccinationSchedules;
        }

        public List<VaccinationSchedule> getUpcomingVaccinationSchedules() {
            return upcomingVaccinationSchedules;
        }
    }
}
