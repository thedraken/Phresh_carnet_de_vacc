package com.phresh;

import com.phresh.domain.*;
import com.phresh.repository.*;
import com.phresh.security.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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
    private final VaccinationScheduleRepository vaccinationScheduleRepository;

    @Autowired
    public AppSetupService(RoleRepository roleRepository, RoleAccessRepository roleAccessRepository, UserRepository userRepository, PasswordEncryptor passwordEncryptor, VaccinationTypeRepository vaccinationTypeRepository, VaccinationRepository vaccinationRepository, DiseaseRepository diseaseRepository, VaccinationScheduleRepository vaccinationScheduleRepository) {
        this.roleRepository = roleRepository;
        this.roleAccessRepository = roleAccessRepository;
        this.userRepository = userRepository;
        this.passwordEncryptor = passwordEncryptor;
        this.vaccinationTypeRepository = vaccinationTypeRepository;
        this.vaccinationRepository = vaccinationRepository;
        this.diseaseRepository = diseaseRepository;
        this.vaccinationScheduleRepository = vaccinationScheduleRepository;
    }

    public void startApp() {

        RoleAccess userRoleAccess = new RoleAccess(RoleAccess.ROLE_USER);
        RoleAccess adminRoleAccess = new RoleAccess(RoleAccess.ROLE_ADMIN);

        roleAccessRepository.saveAll(Arrays.asList(userRoleAccess, adminRoleAccess));

        Role userRole = new Role("member", Collections.singleton(userRoleAccess));
        Role adminRole = new Role("admin", Collections.singleton(adminRoleAccess));
        roleRepository.saveAll(Arrays.asList(userRole, adminRole));


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

        diseaseRepository.saveAll(Arrays.asList(measlesDisease, mumpsDisease, rubellaDisease, covidDisease, meningococcalGrpBDisease,
                meningitisDisease, diphtheriaDisease, hepatitisBDisease, hibDisease, polioDisease, tetanusDisease, whoopingCoughDisease,
                tuberculosisDisease, bronchiolitisDisease, rotavirusDisease, pneumococciDisease, meningocoqueDisease, rougeoleDisease,
                oreillonsDisease, rubeoleDisease, varicelleDisease, meningocoquesACWYDisease));

        VaccinationType mmrVaccinationType = new VaccinationType("MMR", 3 * 365, new HashSet<>(Arrays.asList(measlesDisease, mumpsDisease, rubellaDisease)), 1, null, 2);
        VaccinationType covid19VaccinationType = new VaccinationType("COVID19", 365, new HashSet<>(Collections.singletonList(covidDisease)), null, null, null);
        VaccinationType bcgVaccinationType = new VaccinationType("BCG", null, new HashSet<>(Collections.singletonList(tuberculosisDisease)), null, null, 1);
        VaccinationType sixInOneVaccineType = new VaccinationType("6-in-1", 28, new HashSet<>(Arrays.asList(diphtheriaDisease, hepatitisBDisease, hibDisease, polioDisease, tetanusDisease, whoopingCoughDisease)), 56, 10d, 3);
        VaccinationType threeInOneTeenageBooster = new VaccinationType("3-in-1 teenage booster", null, new HashSet<>(Arrays.asList(diphtheriaDisease, tetanusDisease, polioDisease)), 4745, null, 1);
        VaccinationType rsvVaccinationType = new VaccinationType("RSV", null, new HashSet<>(Collections.singletonList(bronchiolitisDisease)), 0, 0.5, null);
        VaccinationType rotavirusVaccineType = new VaccinationType("Rotavirus Vaccine", 28, new HashSet<>(Collections.singletonList(rotavirusDisease)), 2 * 28, null, 3);
        VaccinationType pneumocoquesVaccineType = new VaccinationType("Pneumocoques Vaccine", 28 * 2, new HashSet<>(Collections.singletonList(pneumococciDisease)), 2 * 28, null, 2);
        VaccinationType pneumocoquesVaccineFinalType = new VaccinationType("Pneumocoques Vaccine Final", null, new HashSet<>(Collections.singletonList(pneumococciDisease)), 11 * 28, null, 1);
        VaccinationType meingocoqueBVaccineType = new VaccinationType("Méningocoque B Vaccine", 2 * 28, new HashSet<>(Collections.singletonList(meningocoqueDisease)), 3 * 28, null, 2);
        VaccinationType rorvCombinationVaccineType = new VaccinationType("RORV Combination Vaccine", null, new HashSet<>(Arrays.asList(rougeoleDisease, oreillonsDisease, rubeoleDisease, varicelleDisease)), 365, null, 1);
        VaccinationType meningocoquesACWYVaccinationType = new VaccinationType("Méningocoques ACWY Vaccine", null, new HashSet<>(Collections.singletonList(meningocoquesACWYDisease)), 13 * 28, null, 1);


        vaccinationTypeRepository.saveAll(Arrays.asList(mmrVaccinationType, covid19VaccinationType, bcgVaccinationType, sixInOneVaccineType,
                threeInOneTeenageBooster, rsvVaccinationType, rotavirusVaccineType, pneumocoquesVaccineType, pneumocoquesVaccineFinalType,
                meingocoqueBVaccineType, rorvCombinationVaccineType, meningocoquesACWYVaccinationType));
    }

    @Transactional
    public void createTestUsers() {
        logger.log(Level.SEVERE, "This is a setup for a test environment and should not be used in a production environment!");
        //TODO Not for production!

        List<Role> roles = roleRepository.findAll();

        Role adminRole = roles.stream().filter(role -> role.getName().equals("admin")).findFirst().orElse(null);
        Role userRole = roles.stream().filter(role -> role.getName().equals("member")).findFirst().orElse(null);

        User userAdmin = new User("Admin", "Admin", "phresh@admin.lu", passwordEncryptor.encode("Admin1-TestSystem"), Collections.singleton(adminRole), LocalDate.of(1970, 1, 1));
        User userWithSomeVaccinesDone = new User("Fred", "Flintstone", "fred.flintstone@post.lu", passwordEncryptor.encode("pebbleRockBamBam1-"), Collections.singleton(userRole), LocalDate.of(1985, 1, 1));
        User babyWithNoVaccines = new User("Rumble", "Flintstone", "rumble.flintstone@post.lu", passwordEncryptor.encode("babyFlinstoneJustBorn2025!"), Collections.singleton(userRole), LocalDate.now());
        logger.log(Level.CONFIG, "Saving users to database...");
        userRepository.saveAll(Arrays.asList(userAdmin, userWithSomeVaccinesDone, babyWithNoVaccines));

        List<VaccinationType> vaccinationTypes = new ArrayList<>();
        vaccinationTypeRepository.findAll().forEach(vaccinationTypes::add);

        List<VaccinationSchedule> babyVaccinationSchedule = setupDefaultVaccinationSchedule(babyWithNoVaccines, vaccinationTypes);
        vaccinationScheduleRepository.saveAll(babyVaccinationSchedule);

        List<VaccinationSchedule> adultVaccinationSchedules = setupDefaultVaccinationSchedule(userWithSomeVaccinesDone, vaccinationTypes);
        vaccinationScheduleRepository.saveAll(adultVaccinationSchedules);

        VaccinationType mmrVaccinationType = vaccinationTypes.stream().filter(vt -> vt.getTypeName().equals("MMR")).findFirst().orElse(null);
        VaccinationType covid19VaccinationType = vaccinationTypes.stream().filter(vt -> vt.getTypeName().equals("COVID19")).findFirst().orElse(null);

        VaccinationSchedule mmrVaccinationScheduleFirst = new VaccinationSchedule(userWithSomeVaccinesDone, mmrVaccinationType, LocalDate.of(1986, 3, 30));
        VaccinationSchedule mmrVaccinationScheduleSecond = new VaccinationSchedule(userWithSomeVaccinesDone, mmrVaccinationType, LocalDate.of(1989, 3, 30));
        vaccinationScheduleRepository.saveAll(Arrays.asList(mmrVaccinationScheduleFirst, mmrVaccinationScheduleSecond));

        List<VaccinationSchedule> covidVaccineSchedules = new ArrayList<>();
        for (int i = 2020; i < 2024; i++) {
            VaccinationSchedule covidVaccinationSchedule = new VaccinationSchedule(userWithSomeVaccinesDone, covid19VaccinationType, LocalDate.of(i, 3, 30));
            vaccinationScheduleRepository.save(covidVaccinationSchedule);
            covidVaccineSchedules.add(covidVaccinationSchedule);
        }


        adultVaccinationSchedules.forEach(vs -> {
            int randomNum = ThreadLocalRandom.current().nextInt(-7, 8);
            Vaccination vaccination = new Vaccination(vs.getVaccinationType(), vs.getScheduledDate().plusDays(randomNum), null, userWithSomeVaccinesDone, vs);
            vs.setVaccination(vaccination);
            vaccinationRepository.save(vaccination);
        });


        Vaccination mmrFirstVaccination = new Vaccination(mmrVaccinationType, LocalDate.of(1986, 4, 15), null, userWithSomeVaccinesDone, mmrVaccinationScheduleFirst);
        mmrVaccinationScheduleFirst.setVaccination(mmrFirstVaccination);
        Vaccination mmrSecondVaccination = new Vaccination(mmrVaccinationType, LocalDate.of(1989, 5, 20), null, userWithSomeVaccinesDone, mmrVaccinationScheduleSecond);
        mmrVaccinationScheduleSecond.setVaccination(mmrSecondVaccination);
        Vaccination covidFirstVaccination = new Vaccination(covid19VaccinationType, LocalDate.of(2020, 11, 15), null, userWithSomeVaccinesDone, covidVaccineSchedules.get(0));
        covidVaccineSchedules.get(0).setVaccination(covidFirstVaccination);
        Vaccination covidSecondVaccination = new Vaccination(covid19VaccinationType, LocalDate.of(2021, 11, 18), null, userWithSomeVaccinesDone, covidVaccineSchedules.get(1));
        covidVaccineSchedules.get(1).setVaccination(covidSecondVaccination);
        Vaccination covidThirdVaccination = new Vaccination(covid19VaccinationType, LocalDate.of(2022, 12, 8), "Minor reaction, sent for more analysis", userWithSomeVaccinesDone, covidVaccineSchedules.get(2));
        covidVaccineSchedules.get(2).setVaccination(covidThirdVaccination);

        logger.log(Level.CONFIG, "Saving vaccination history to database...");
        vaccinationRepository.saveAll(Arrays.asList(mmrFirstVaccination, mmrSecondVaccination, covidFirstVaccination, covidSecondVaccination, covidThirdVaccination));
    }

    private List<VaccinationSchedule> setupDefaultVaccinationSchedule(User user, List<VaccinationType> vaccinationTypes) {
        List<VaccinationSchedule> vaccinationSchedules = new ArrayList<>();
        VaccinationType rsvVaccinationType = vaccinationTypes.stream().filter(vt -> vt.getTypeName().equals("RSV")).findAny().orElse(null);
        VaccinationType sixInOneVaccineType = vaccinationTypes.stream().filter(vt -> vt.getTypeName().equals("6-in-1")).findAny().orElse(null);
        VaccinationType rotavirusVaccineType = vaccinationTypes.stream().filter(vt -> vt.getTypeName().equals("Rotavirus Vaccine")).findAny().orElse(null);
        VaccinationType pneumocoquesVaccineType = vaccinationTypes.stream().filter(vt -> vt.getTypeName().equals("Pneumocoques Vaccine")).findAny().orElse(null);
        VaccinationType meingocoqueBVaccineType = vaccinationTypes.stream().filter(vt -> vt.getTypeName().equals("Méningocoque B Vaccine")).findAny().orElse(null);
        VaccinationType pneumocoquesVaccineFinalType = vaccinationTypes.stream().filter(vt -> vt.getTypeName().equals("Pneumocoques Vaccine Final")).findAny().orElse(null);
        VaccinationType rorvCombinationVaccineType = vaccinationTypes.stream().filter(vt -> vt.getTypeName().equals("RORV Combination Vaccine")).findAny().orElse(null);
        VaccinationType meningocoquesACWYVaccinationType = vaccinationTypes.stream().filter(vt -> vt.getTypeName().equals("Méningocoques ACWY Vaccine")).findAny().orElse(null);

        vaccinationSchedules.add(new VaccinationSchedule(user, rsvVaccinationType, user.getDateOfBirth()));
        vaccinationSchedules.add(new VaccinationSchedule(user, sixInOneVaccineType, user.getDateOfBirth().plusDays(2 * 28)));
        vaccinationSchedules.add(new VaccinationSchedule(user, rotavirusVaccineType, user.getDateOfBirth().plusDays(2 * 28)));
        vaccinationSchedules.add(new VaccinationSchedule(user, pneumocoquesVaccineType, user.getDateOfBirth().plusDays(2 * 28)));
        vaccinationSchedules.add(new VaccinationSchedule(user, rotavirusVaccineType, user.getDateOfBirth().plusDays(3 * 28)));
        vaccinationSchedules.add(new VaccinationSchedule(user, meingocoqueBVaccineType, user.getDateOfBirth().plusDays(3 * 28)));
        vaccinationSchedules.add(new VaccinationSchedule(user, sixInOneVaccineType, user.getDateOfBirth().plusDays(4 * 28)));
        vaccinationSchedules.add(new VaccinationSchedule(user, pneumocoquesVaccineType, user.getDateOfBirth().plusDays(4 * 28)));
        vaccinationSchedules.add(new VaccinationSchedule(user, rotavirusVaccineType, user.getDateOfBirth().plusDays(4 * 28)));
        vaccinationSchedules.add(new VaccinationSchedule(user, meingocoqueBVaccineType, user.getDateOfBirth().plusDays(5 * 28)));
        //Only if not received at birth
        //vaccinationSchedules.add(new VaccinationSchedule(user, rsvVaccinationType, user.getDateOfBirth().plusDays(5*28)));
        vaccinationSchedules.add(new VaccinationSchedule(user, sixInOneVaccineType, user.getDateOfBirth().plusDays(11 * 28)));
        vaccinationSchedules.add(new VaccinationSchedule(user, pneumocoquesVaccineFinalType, user.getDateOfBirth().plusDays(11 * 28)));
        vaccinationSchedules.add(new VaccinationSchedule(user, rorvCombinationVaccineType, user.getDateOfBirth().plusDays(12 * 28)));
        vaccinationSchedules.add(new VaccinationSchedule(user, meningocoquesACWYVaccinationType, user.getDateOfBirth().plusDays(13 * 28)));
        return vaccinationSchedules;
    }
}
