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

    public void startApp(boolean createTestUsers) {

        RoleAccess userRoleAccess = new RoleAccess(RoleAccess.ROLE_USER);
        RoleAccess adminRoleAccess = new RoleAccess(RoleAccess.ROLE_ADMIN);
        roleAccessRepository.saveAll(List.of(userRoleAccess, adminRoleAccess));

        Role userRole = new Role("member", Set.of(userRoleAccess));
        Role adminRole = new Role("admin", Set.of(adminRoleAccess));
        roleRepository.saveAll(List.of(userRole, adminRole));


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
        Disease bronchiolitisDisease = new Disease("Bronchiolitis");
        Disease rotavirusDisease = new Disease("Rotavirus");
        Disease pneumococciDisease = new Disease("Pneumococci");
        Disease meningocoqueDisease = new Disease("Méningocoque");
        Disease rougeoleDisease = new Disease("Rougeole");
        Disease oreillonsDisease = new Disease("Oreillons");
        Disease rubeoleDisease = new Disease("Rubéole");
        Disease varicelleDisease = new Disease("Varicelle");
        Disease meningocoquesACWYDisease = new Disease("Méningocoques ACWY");

        diseaseRepository.saveAll(List.of(measlesDisease, mumpsDisease, rubellaDisease, covidDisease, meningococcalGrpBDisease,
                meningitisDisease, diphtheriaDisease, hepatitisBDisease, hibDisease, polioDisease, tetanusDisease, whoopingCoughDisease,
                tuberculosisDisease, bronchiolitisDisease, rotavirusDisease, pneumococciDisease, meningocoqueDisease, rougeoleDisease,
                oreillonsDisease, rubeoleDisease, varicelleDisease, meningocoquesACWYDisease));

        VaccinationType mmrVaccinationType = new VaccinationType("MMR", 3 * 365, Set.of(measlesDisease, mumpsDisease, rubellaDisease), 1, null, 2);
        VaccinationType covid19VaccinationType = new VaccinationType("COVID19", 365, Set.of(covidDisease), null, null, null);
        VaccinationType bcgVaccinationType = new VaccinationType("BCG", null, Set.of(tuberculosisDisease), null, null, 1);
        VaccinationType sixInOneVaccine = new VaccinationType("6-in-1", 28, Set.of(diphtheriaDisease, hepatitisBDisease, hibDisease, polioDisease, tetanusDisease, whoopingCoughDisease), 56, 10d, 3);
        VaccinationType threeInOneTeenageBooster = new VaccinationType("3-in-1 teenage booster", null, Set.of(diphtheriaDisease, tetanusDisease, polioDisease), 4745, null, 1);
        VaccinationType rsvVaccinationType = new VaccinationType("RSV", null, Set.of(bronchiolitisDisease), 0, 0.5, null);
        VaccinationType rotavirusVaccineType = new VaccinationType("Rotavirus Vaccine", 28, Set.of(rotavirusDisease), 2 * 28, null, 3);
        VaccinationType pneumocoquesVaccineType = new VaccinationType("Pneumocoques Vaccine", 28 * 2, Set.of(pneumococciDisease), 2 * 28, null, 2);
        VaccinationType pneumocoquesVaccineFinalType = new VaccinationType("Pneumocoques Vaccine Final", null, Set.of(pneumococciDisease), 11 * 28, null, 1);
        VaccinationType meingocoqueVaccineType = new VaccinationType("Méningocoque Vaccine", 2 * 28, Set.of(meningocoqueDisease), 3 * 28, null, 2);
        VaccinationType rorvCombinationVaccineType = new VaccinationType("RORV Combination Vaccine", null, Set.of(rougeoleDisease, oreillonsDisease, rubeoleDisease, varicelleDisease), 365, null, 1);
        VaccinationType meningocoquesACWYVaccinationType = new VaccinationType("Méningocoques ACWY Vaccine", null, Set.of(meningocoquesACWYDisease), 13 * 28, null, 1);


        vaccinationTypeRepository.saveAll(List.of(mmrVaccinationType, covid19VaccinationType, bcgVaccinationType, sixInOneVaccine,
                threeInOneTeenageBooster, rsvVaccinationType, rotavirusVaccineType, pneumocoquesVaccineType, pneumocoquesVaccineFinalType,
                meingocoqueVaccineType, rorvCombinationVaccineType, meningocoquesACWYVaccinationType));

        if (createTestUsers) {
            logger.log(Level.SEVERE, "This is a setup for a test environment and should not be used in a production environment!");
            //TODO Not for production!
            User userAdmin = new User("Admin", "Admin", "phresh@admin.lu", passwordEncryptor.encode("Admin1-TestSystem"), Set.of(adminRole), LocalDate.of(1970, 1, 1));
            User userWithSomeVaccinesDone = new User("Fred", "Flintstone", "fred.flintstone@post.lu", passwordEncryptor.encode("pebbleRockBamBam1-"), Set.of(userRole), LocalDate.of(1985, 1, 1));
            User babyWithNoVaccines = new User("Rumble", "Flintstone", "rumble.flintstone@post.lu", passwordEncryptor.encode("babyFlinstoneJustBorn2025!"), Set.of(userRole), LocalDate.now());
            logger.log(Level.CONFIG, "Saving users to database...");
            userRepository.saveAll(List.of(userAdmin, userWithSomeVaccinesDone, babyWithNoVaccines));

            Vaccination mmrFirstVaccination = new Vaccination(mmrVaccinationType, LocalDate.of(1986, 4, 15), null, LocalDate.of(1989, 4, 15), userWithSomeVaccinesDone);
            Vaccination mmrSecondVaccination = new Vaccination(mmrVaccinationType, LocalDate.of(1989, 5, 20), null, null, userWithSomeVaccinesDone);
            Vaccination covidFirstVaccination = new Vaccination(covid19VaccinationType, LocalDate.of(2020, 11, 15), null, LocalDate.of(2021, 11, 15), userWithSomeVaccinesDone);
            Vaccination covidSecondVaccination = new Vaccination(covid19VaccinationType, LocalDate.of(2021, 11, 18), null, LocalDate.of(2022, 11, 18), userWithSomeVaccinesDone);
            Vaccination covidThirdVaccination = new Vaccination(covid19VaccinationType, LocalDate.of(2022, 12, 8), "Minor reaction, sent for more analysis", LocalDate.of(2023, 12, 8), userWithSomeVaccinesDone);

            logger.log(Level.CONFIG, "Saving vaccination history to database...");
            vaccinationRepository.saveAll(List.of(mmrFirstVaccination, mmrSecondVaccination, covidFirstVaccination, covidSecondVaccination, covidThirdVaccination));
        }


    }
}
