import com.phresh.domain.User;
import com.phresh.domain.Vaccination;
import com.phresh.domain.VaccinationType;
import com.phresh.presenter.VaccinationCardPresenter;
import com.phresh.view.VaccinationCardView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class VaccinationCardViewTest {

    @Mock
    private VaccinationCardPresenter vaccinationCardPresenter;

    @InjectMocks
    private VaccinationCardView vaccinationCardView;

    public void setup() {
        User user = Mockito.mock(User.class);
        Mockito.when(vaccinationCardPresenter.getUserFromAuthenticationContext()).thenReturn(user);
    }

    //@Test
    public void testCheckMatches() {
        setup();

        Vaccination item = Mockito.mock(Vaccination.class);
        VaccinationType type = Mockito.mock(VaccinationType.class);
        Mockito.when(item.getVaccinationType()).thenReturn(type);
        Mockito.when(item.getDateOfVaccination()).thenReturn(LocalDate.of(2000, 1, 2));
        Mockito.when(type.getTypeName()).thenReturn("TEST");

        Assertions.assertTrue(vaccinationCardView.checkMatches(item, "TE"));
        Assertions.assertTrue(vaccinationCardView.checkMatches(item, "TEST"));
        Assertions.assertFalse(vaccinationCardView.checkMatches(item, "TEST2"));
        Assertions.assertTrue(vaccinationCardView.checkMatches(item, "01"));
        Assertions.assertTrue(vaccinationCardView.checkMatches(item, "02"));
        Assertions.assertTrue(vaccinationCardView.checkMatches(item, "20"));
    }
}
