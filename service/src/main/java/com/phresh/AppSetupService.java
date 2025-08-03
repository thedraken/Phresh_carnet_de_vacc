package com.phresh;

import com.phresh.domain.*;
import com.phresh.repository.*;
import com.phresh.security.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AppSetupService {

    private static final Logger logger = Logger.getLogger(AppSetupService.class.getSimpleName());

    private final RoleRepository roleRepository;
    private final RoleAccessRepository roleAccessRepository;
    private final UserRepository userRepository;
    private final PasswordEncryptor passwordEncryptor;
    private final VaccinationTypeRepository vaccinationTypeRepository;
    private final VaccinationRepository vaccinationRepository;
    private final DiseaseRepository diseaseRepository;

    @Autowired
    public AppSetupService(RoleRepository roleRepository, RoleAccessRepository roleAccessRepository, UserRepository userRepository, PasswordEncryptor passwordEncryptor, VaccinationTypeRepository vaccinationTypeRepository, VaccinationRepository vaccinationRepository, DiseaseRepository diseaseRepository) {
        this.roleRepository = roleRepository;
        this.roleAccessRepository = roleAccessRepository;
        this.userRepository = userRepository;
        this.passwordEncryptor = passwordEncryptor;
        this.vaccinationTypeRepository = vaccinationTypeRepository;
        this.vaccinationRepository = vaccinationRepository;
        this.diseaseRepository = diseaseRepository;
    }

    public void startApp() {
        logger.log(Level.SEVERE, "This is a setup for a test environment and should not be used in a production environment!");

        RoleAccess userRoleAccess = new RoleAccess(RoleAccess.ROLE_USER);
        RoleAccess adminRoleAccess = new RoleAccess(RoleAccess.ROLE_ADMIN);
        roleAccessRepository.saveAll(List.of(userRoleAccess, adminRoleAccess));

        Role userRole = new Role("member", Set.of(userRoleAccess));
        Role adminRole = new Role("admin", Set.of(adminRoleAccess));
        roleRepository.saveAll(List.of(userRole, adminRole));

        //TODO Not for production!
        User userAdmin = new User("Admin", "Admin", "phresh@admin.lu", passwordEncryptor.encode("Admin1-TestSystem"), Set.of(adminRole));
        User userWithVaccinesDone = new User("Fred", "Flintstone", "fred.flintstone@post.lu", passwordEncryptor.encode("pebbleRockBamBam1-"), Set.of(userRole));
        userRepository.saveAll(List.of(userAdmin, userWithVaccinesDone));

        Disease measlesDisease = new Disease("Measles");
        Disease mumpsDisease = new Disease("Mumps");
        Disease rubellaDisease = new Disease("Rubella");
        Disease covidDisease = new Disease("Covid19");
        Disease meningococcalGrpBDisease = new Disease("Meningococcal group B");
        Disease meningitisDisease = new Disease("Meningitis");
        Disease diphtheriaDisease = new Disease("Diphtheria");
        Disease hepatitisBDisease = new Disease("Hepatitis B");
        Disease hibDisease = new Disease("Haemophilus influenzae type b");
        Disease polioDisease = new Disease("Polio");
        Disease tetanusDisease = new Disease("Tetanus");
        Disease whoopingCoughDisease = new Disease("Whooping cough");
        Disease tuberculosisDisease = new Disease("Tuberculosis");
        diseaseRepository.saveAll(List.of(measlesDisease, mumpsDisease, rubellaDisease, covidDisease, meningococcalGrpBDisease, meningitisDisease, diphtheriaDisease, hepatitisBDisease, hibDisease, polioDisease, tetanusDisease, whoopingCoughDisease, tuberculosisDisease));

        VaccinationType mmrVaccinationType = new VaccinationType("MMR", 3 * 365, Set.of(measlesDisease, mumpsDisease, rubellaDisease), 1, null, 2);
        VaccinationType covid19VaccinationType = new VaccinationType("COVID19", 365, Set.of(covidDisease), null, null, null);
        VaccinationType bcgVaccinationType = new VaccinationType("BCG", null, Set.of(tuberculosisDisease), null, null, 1);
        VaccinationType sixInOneVaccine = new VaccinationType("6-in-1", 28, Set.of(diphtheriaDisease, hepatitisBDisease, hibDisease, polioDisease, tetanusDisease, whoopingCoughDisease), 56, 10, 3);
        VaccinationType threeInOneTeenageBooster = new VaccinationType("3-in-1 teenage booster", null, Set.of(diphtheriaDisease, tetanusDisease, polioDisease), 4745, null, 1);

        vaccinationTypeRepository.saveAll(List.of(mmrVaccinationType, covid19VaccinationType, bcgVaccinationType, sixInOneVaccine, threeInOneTeenageBooster));


        Vaccination mmrFirstVaccination = new Vaccination(mmrVaccinationType, LocalDate.of(1986, 4, 15), null, LocalDate.of(1989, 4, 15), List.of(userWithVaccinesDone));
        Vaccination mmrSecondVaccination = new Vaccination(mmrVaccinationType, LocalDate.of(1989, 5, 20), null, null, List.of(userWithVaccinesDone));
        Vaccination covidFirstVaccination = new Vaccination(covid19VaccinationType, LocalDate.of(2020, 11, 15), null, LocalDate.of(2021, 11, 15), List.of(userWithVaccinesDone));
        Vaccination covidSecondVaccination = new Vaccination(covid19VaccinationType, LocalDate.of(2021, 11, 18), null, LocalDate.of(2022, 11, 18), List.of(userWithVaccinesDone));
        Vaccination covidThirdVaccination = new Vaccination(covid19VaccinationType, LocalDate.of(2022, 12, 8), null, LocalDate.of(2023, 12, 8), List.of(userWithVaccinesDone));

        vaccinationRepository.saveAll(List.of(mmrFirstVaccination, mmrSecondVaccination, covidFirstVaccination, covidSecondVaccination, covidThirdVaccination));

    }
}
