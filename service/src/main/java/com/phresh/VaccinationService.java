package com.phresh;

import com.phresh.domain.Vaccination;
import com.phresh.exceptions.RuleException;
import com.phresh.repository.VaccinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.logging.Logger;

@Service
public class VaccinationService {

    private static final Logger logger = Logger.getLogger(VaccinationService.class.getSimpleName());

    private final VaccinationRepository vaccinationRepository;

    @Autowired
    public VaccinationService(VaccinationRepository vaccinationRepository) {
        this.vaccinationRepository = vaccinationRepository;
    }

    public void saveVaccination(Vaccination vaccination) throws RuleException {
        if (vaccination.getVaccinationType() == null) {
            throw new RuleException("Please enter a vaccination type");
        }
        if (vaccination.getDateOfVaccination() == null) {
            throw new RuleException("Please enter a date of vaccination");
        }
        if (vaccination.getDateOfVaccination().isAfter(LocalDate.now())) {
            throw new RuleException("Date of vaccination is before current date");
        }
        if (vaccination.getDateOfVaccination().isBefore(vaccination.getUser().getDateOfBirth())) {
            throw new RuleException("Date of vaccination is before your date of birth");
        }
        Integer numberOfVaccines = vaccinationRepository.countVaccinationByVaccinationTypeAndUser(vaccination.getVaccinationType(), vaccination.getUser());

        if (numberOfVaccines != null && ++numberOfVaccines > vaccination.getVaccinationType().getMaxNoOfDoses()) {
            throw new RuleException("You have already received the maximum number of vaccines of " + vaccination.getVaccinationType().getTypeName());
        }

        if (vaccination.getVaccinationType().getNumberOfDaysBeforeNextVaccination() != null) {
            vaccination.setDateOfRenewal(vaccination.getDateOfVaccination().plusDays(vaccination.getVaccinationType().getNumberOfDaysBeforeNextVaccination()));
        }

        Double maxAge = vaccination.getVaccinationType().getMaxAgeForVaccineInYears();
        if (maxAge != null) {
            if (vaccination.getUser().getAge() > maxAge) {
                throw new RuleException("You are too old to receive " + vaccination.getVaccinationType().getTypeName() + " (Max age: " + maxAge + ", Your age: " + vaccination.getUser().getAge() + ")");
            }
        }

        vaccinationRepository.save(vaccination);
        logger.config("Saved vaccination: " + vaccination.getId());
    }
}
