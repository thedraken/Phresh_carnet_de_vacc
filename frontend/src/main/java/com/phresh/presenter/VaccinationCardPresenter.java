package com.phresh.presenter;

import com.google.common.collect.ImmutableList;
import com.phresh.VaccinationService;
import com.phresh.domain.*;
import com.phresh.exceptions.RuleException;
import com.phresh.repository.DiseaseRepository;
import com.phresh.repository.VaccinationRepository;
import com.phresh.repository.VaccinationScheduleRepository;
import com.phresh.repository.VaccinationTypeRepository;
import com.phresh.view.VaccinationCardView;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class VaccinationCardPresenter extends AbstractLoggedinPresenter<VaccinationCardView> {

    private static final Logger logger = Logger.getLogger(VaccinationCardPresenter.class.getSimpleName());

    private final VaccinationRepository vaccinationRepository;
    private final VaccinationTypeRepository vaccinationTypeRepository;
    private final DiseaseRepository diseaseRepository;
    private final VaccinationService vaccinationService;
    private final List<VaccineScheduleCache> cache = new ArrayList<>();

    @Autowired
    public VaccinationCardPresenter(AuthenticationContext authenticationContext, VaccinationRepository vaccinationRepository, VaccinationTypeRepository vaccinationTypeRepository, DiseaseRepository diseaseRepository, VaccinationService vaccinationService, VaccinationScheduleRepository vaccinationScheduleRepository) {
        super(authenticationContext, vaccinationScheduleRepository);
        this.vaccinationRepository = vaccinationRepository;
        this.vaccinationTypeRepository = vaccinationTypeRepository;
        this.diseaseRepository = diseaseRepository;
        this.vaccinationService = vaccinationService;
    }

    public List<Vaccination> doSearch(String freeText, LocalDate startDate, LocalDate endDate, Disease disease, VaccinationType vaccinationType) {
        logger.info("Do search for vaccinations");
        return vaccinationRepository.findVaccinationsByFilter(freeText, startDate, endDate, disease, vaccinationType, getUserFromAuthenticationContext());
    }

    public List<Disease> getAllDiseases() {
        return ImmutableList.copyOf(diseaseRepository.findAll());
    }

    public List<VaccinationType> getAllVaccinationTypes() {
        return ImmutableList.copyOf(vaccinationTypeRepository.findAll());
    }

    public void saveVaccination(Vaccination vaccination) throws RuleException {
        vaccination.setUser(getUserFromAuthenticationContext());
        vaccinationService.saveVaccination(vaccination);
    }

    public VaccinationSchedule getNextVaccinationSchedule(Vaccination vaccination) {
        cache.removeIf(VaccineScheduleCache::isExpired);
        List<VaccineScheduleCache> vaccinationScheduleCache = cache.stream().filter(vs -> vs.user.equals(vaccination.getUser()) && vs.vaccinationType.equals(vaccination.getVaccinationType())).collect(Collectors.toList());
        List<VaccinationSchedule> vaccinationSchedules;
        if (vaccinationScheduleCache.isEmpty()) {
            vaccinationSchedules = vaccinationScheduleRepository.findAllByVaccinationTypeAndUserAndVaccinationIsNull(vaccination.getVaccinationType(), vaccination.getUser());
            cache.add(new VaccineScheduleCache(vaccination.getUser(), vaccination.getVaccinationType(), vaccinationSchedules));
        } else {
            vaccinationSchedules = vaccinationScheduleCache.stream().map(VaccineScheduleCache::getVaccinationSchedules).flatMap(List::stream).collect(Collectors.toList());
        }
        return vaccinationSchedules.stream().min(Comparator.comparing(VaccinationSchedule::getScheduledDate)).orElse(null);
    }

    private static class VaccineScheduleCache {
        private final User user;
        private final VaccinationType vaccinationType;
        private final List<VaccinationSchedule> vaccinationSchedules;
        private final LocalDateTime creationDateTime;

        public VaccineScheduleCache(User user, VaccinationType vaccinationType, List<VaccinationSchedule> vaccinationSchedules) {
            this.user = user;
            this.vaccinationType = vaccinationType;
            this.vaccinationSchedules = vaccinationSchedules;
            this.creationDateTime = LocalDateTime.now();
        }

        public User getUser() {
            return user;
        }

        public VaccinationType getVaccinationType() {
            return vaccinationType;
        }

        public List<VaccinationSchedule> getVaccinationSchedules() {
            return vaccinationSchedules;
        }

        public LocalDateTime getCreationDateTime() {
            return creationDateTime;
        }

        public boolean isExpired() {
            return creationDateTime.plusMinutes(1).isAfter(LocalDateTime.now());
        }
    }

}
