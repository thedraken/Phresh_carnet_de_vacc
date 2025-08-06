package com.phresh.presenter;

import com.phresh.domain.VaccinationSchedule;
import com.phresh.repository.VaccinationScheduleRepository;
import com.phresh.view.ScheduledVaccinationView;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledVaccinationPresenter extends AbstractLoggedinPresenter<ScheduledVaccinationView> {

    @Autowired
    public ScheduledVaccinationPresenter(AuthenticationContext authenticationContext, VaccinationScheduleRepository vaccinationScheduleRepository) {
        super(authenticationContext, vaccinationScheduleRepository);
    }

    public List<VaccinationSchedule> getVaccinationSchedules() {
        return vaccinationScheduleRepository.findAllByUserAndVaccinationIsNull(getUserFromAuthenticationContext());
    }
}
