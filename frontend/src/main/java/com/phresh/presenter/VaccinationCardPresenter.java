package com.phresh.presenter;

import com.google.common.collect.ImmutableList;
import com.phresh.VaccinationService;
import com.phresh.domain.Disease;
import com.phresh.domain.Vaccination;
import com.phresh.domain.VaccinationType;
import com.phresh.exceptions.RuleException;
import com.phresh.repository.DiseaseRepository;
import com.phresh.repository.VaccinationRepository;
import com.phresh.repository.VaccinationTypeRepository;
import com.phresh.view.VaccinationCardView;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Component
public class VaccinationCardPresenter extends AbstractLoggedinPresenter<VaccinationCardView> {

    private static final Logger logger = Logger.getLogger(VaccinationCardPresenter.class.getSimpleName());

    private final VaccinationRepository vaccinationRepository;
    private final VaccinationTypeRepository vaccinationTypeRepository;
    private final DiseaseRepository diseaseRepository;
    private final VaccinationService vaccinationService;

    @Autowired
    public VaccinationCardPresenter(AuthenticationContext authenticationContext, VaccinationRepository vaccinationRepository, VaccinationTypeRepository vaccinationTypeRepository, DiseaseRepository diseaseRepository, VaccinationService vaccinationService) {
        super(authenticationContext);
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

}
