package com.phresh;

import com.phresh.domain.User;
import com.phresh.domain.Vaccination;
import com.phresh.domain.VaccinationType;
import com.phresh.exceptions.RuleException;
import com.phresh.repository.VaccinationRepository;
import com.phresh.repository.VaccinationScheduleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class VaccinationServiceTest {

    @Mock
    private VaccinationRepository vaccinationRepository;
    @Mock
    private VaccinationScheduleRepository vaccinationScheduleRepository;

    @InjectMocks
    private VaccinationService vaccinationService;

    @Test
    public void saveVaccination() throws RuleException {
        Exception exception = Assertions.assertThrows(RuleException.class, () -> vaccinationService.saveVaccination(null));
        Assertions.assertEquals("Missing vaccination details", exception.getMessage());

        Vaccination vaccination = new Vaccination();
        exception = Assertions.assertThrows(RuleException.class, () -> vaccinationService.saveVaccination(vaccination));
        Assertions.assertEquals("Please enter a vaccination type", exception.getMessage());

        VaccinationType vaccinationType = Mockito.mock(VaccinationType.class);
        Mockito.when(vaccinationType.getMaxNoOfDoses()).thenReturn(null);
        Mockito.when(vaccinationType.getMaxAgeForVaccineInYears()).thenReturn(null);
        Mockito.when(vaccinationType.getNumberOfDaysBeforeNextVaccination()).thenReturn(null);
        vaccination.setVaccinationType(vaccinationType);
        exception = Assertions.assertThrows(RuleException.class, () -> vaccinationService.saveVaccination(vaccination));
        Assertions.assertEquals("Missing user details", exception.getMessage());

        User user = Mockito.mock(User.class);
        Mockito.when(user.getDateOfBirth()).thenReturn(LocalDate.now().minusYears(30));
        vaccination.setUser(user);
        exception = Assertions.assertThrows(RuleException.class, () -> vaccinationService.saveVaccination(vaccination));
        Assertions.assertEquals("Please enter a date of vaccination", exception.getMessage());

        vaccination.setDateOfVaccination(LocalDate.now().plusDays(1));
        exception = Assertions.assertThrows(RuleException.class, () -> vaccinationService.saveVaccination(vaccination));
        Assertions.assertEquals("Date of vaccination is after current date", exception.getMessage());

        vaccination.setDateOfVaccination(LocalDate.now().minusYears(31));
        exception = Assertions.assertThrows(RuleException.class, () -> vaccinationService.saveVaccination(vaccination));
        Assertions.assertEquals("Date of vaccination is before your date of birth", exception.getMessage());

        vaccination.setDateOfVaccination(LocalDate.now().minusYears(2));

        vaccinationService.saveVaccination(vaccination);
        Mockito.verify(vaccinationRepository).save(vaccination);
    }
}
