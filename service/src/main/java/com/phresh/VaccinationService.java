package com.phresh;

import com.phresh.domain.Vaccination;
import com.phresh.domain.VaccinationSchedule;
import com.phresh.exceptions.RuleException;
import com.phresh.repository.VaccinationRepository;
import com.phresh.repository.VaccinationScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.logging.Logger;

@Service
public class VaccinationService {

    private static final Logger logger = Logger.getLogger(VaccinationService.class.getSimpleName());

    private final VaccinationRepository vaccinationRepository;
    private final VaccinationScheduleRepository vaccinationScheduleRepository;

    @Autowired
    public VaccinationService(VaccinationRepository vaccinationRepository, VaccinationScheduleRepository vaccinationScheduleRepository) {
        this.vaccinationRepository = vaccinationRepository;
        this.vaccinationScheduleRepository = vaccinationScheduleRepository;
    }

    @Transactional(rollbackFor = RuleException.class)
    public void saveVaccination(Vaccination vaccination) throws RuleException {
        if (vaccination == null) {
            throw new RuleException("Missing vaccination details");
        }
        if (vaccination.getVaccinationType() == null) {
            throw new RuleException("Please enter a vaccination type");
        }
        if (vaccination.getUser() == null) {
            throw new RuleException("Missing user details");
        }
        if (vaccination.getDateOfVaccination() == null) {
            throw new RuleException("Please enter a date of vaccination");
        }
        if (vaccination.getDateOfVaccination().isAfter(LocalDate.now())) {
            throw new RuleException("Date of vaccination is after current date");
        }
        if (vaccination.getDateOfVaccination().isBefore(vaccination.getUser().getDateOfBirth())) {
            throw new RuleException("Date of vaccination is before your date of birth");
        }

        Integer numberOfVaccines = vaccinationRepository.countVaccinationByVaccinationTypeAndUser(vaccination.getVaccinationType(), vaccination.getUser());

        if (numberOfVaccines != null && vaccination.getVaccinationType().getMaxNoOfDoses() != null && ++numberOfVaccines > vaccination.getVaccinationType().getMaxNoOfDoses()) {
            throw new RuleException("You have already received the maximum number of vaccines of " + vaccination.getVaccinationType().getTypeName());
        }

        Double maxAge = vaccination.getVaccinationType().getMaxAgeForVaccineInYears();
        if (maxAge != null) {
            if (vaccination.getUser().getAge() > maxAge) {
                throw new RuleException("You are too old to receive " + vaccination.getVaccinationType().getTypeName() + " (Max age: " + maxAge + ", Your age: " + vaccination.getUser().getAge() + ")");
            }
        }

        if (vaccination.getVaccinationType().getNumberOfDaysBeforeNextVaccination() != null) {
            VaccinationSchedule vaccinationSchedule = new VaccinationSchedule(vaccination.getUser(), vaccination.getVaccinationType(), vaccination.getDateOfVaccination().plusDays(vaccination.getVaccinationType().getNumberOfDaysBeforeNextVaccination()));
            vaccinationScheduleRepository.save(vaccinationSchedule);
        }

        vaccinationRepository.save(vaccination);
        logger.config("Saved vaccination: " + vaccination.getId());
    }
}
