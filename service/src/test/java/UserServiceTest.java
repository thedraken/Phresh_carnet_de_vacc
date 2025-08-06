import com.phresh.UserService;
import com.phresh.domain.Role;
import com.phresh.domain.User;
import com.phresh.exceptions.RuleException;
import com.phresh.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    public void testSaveUser() throws RuleException {
        Exception exception = assertThrows(RuleException.class, () -> userService.saveUser(null));
        Assertions.assertEquals("Missing user details", exception.getMessage());

        User user = new User();
        exception = assertThrows(RuleException.class, () -> userService.saveUser(user));
        Assertions.assertEquals("Please enter first name", exception.getMessage());

        user.setFirstName("First Name");
        exception = assertThrows(RuleException.class, () -> userService.saveUser(user));
        Assertions.assertEquals("Please enter surname", exception.getMessage());

        user.setSurname("Surname");
        exception = assertThrows(RuleException.class, () -> userService.saveUser(user));
        Assertions.assertEquals("Please enter email", exception.getMessage());

        user.setEmail("Email");
        exception = assertThrows(RuleException.class, () -> userService.saveUser(user));
        Assertions.assertEquals("Please enter password", exception.getMessage());

        user.setPassword("Password");
        exception = assertThrows(RuleException.class, () -> userService.saveUser(user));
        Assertions.assertEquals("Missing role, please contact support", exception.getMessage());

        user.setRoles(Set.of(new Role()));
        userService.saveUser(user);

        verify(userRepository).findUserByEmail(user.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    public void testDeleteUser() throws RuleException {
        Exception exception = assertThrows(RuleException.class, () -> userService.deleteUser(null));
        Assertions.assertEquals("Missing user details", exception.getMessage());

        User user = Mockito.mock(User.class);
        when(user.getRoles()).thenReturn(Set.of(new Role()));
        when(user.getFirstName()).thenReturn("First Name");
        when(user.getSurname()).thenReturn("Surname");
        when(user.getEmail()).thenReturn("Email");
        when(user.getPassword()).thenReturn("Password");
        userService.deleteUser(user);
        verify(user).setEnabled(false);
    }
}
